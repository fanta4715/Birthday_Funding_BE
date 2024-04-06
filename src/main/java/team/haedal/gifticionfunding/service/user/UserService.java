package team.haedal.gifticionfunding.service.user;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import team.haedal.gifticionfunding.dto.user.request.UserJoinRequest;
import team.haedal.gifticionfunding.dto.user.response.UserJoinResponse;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.handler.ex.CustomApiException;
import team.haedal.gifticionfunding.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserJoinResponse join(UserJoinRequest joinRequest) {
        Optional<User> userOP = userRepository.findByEmail(joinRequest.email());
        if (userOP.isPresent()) {
            throw new CustomApiException("동일한 email이 존재합니다");
        }

        User userPS = userRepository.save(joinRequest.toEntity(passwordEncoder));
        return new UserJoinResponse(userPS.getId(), userPS.getEmail(), userPS.getCreatedDate());
    }
}
