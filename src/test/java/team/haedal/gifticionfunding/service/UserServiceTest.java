package team.haedal.gifticionfunding.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.haedal.gifticionfunding.dto.user.request.UserJoinRequest;
import team.haedal.gifticionfunding.dto.user.response.UserJoinResponse;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.repository.user.UserRepository;
import team.haedal.gifticionfunding.service.user.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 회원가입_test() throws Exception {
        // given
        UserJoinRequest userJoinRequest = new UserJoinRequest(
                "jaehyeon@naver.com",
                "1234",
                "jaehyeon",
                LocalDate.of(2000,11,14),
                null);

        // stub 1
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // stub 2
        User jaehyeon = userJoinRequest.toEntity(passwordEncoder);
        when(userRepository.save(any())).thenReturn(jaehyeon);

        // when
        UserJoinResponse userJoinResponse = userService.join(userJoinRequest);

        // then
        assertThat(userJoinResponse.email()).isEqualTo("jaehyeon@naver.com");
    }
}