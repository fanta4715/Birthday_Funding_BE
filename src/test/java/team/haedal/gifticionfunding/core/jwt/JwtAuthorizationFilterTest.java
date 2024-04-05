package team.haedal.gifticionfunding.core.jwt;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import team.haedal.gifticionfunding.core.security.auth.PrincipalDetails;
import team.haedal.gifticionfunding.dummy.DummyFactory;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.repository.user.UserRepository;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class JwtAuthorizationFilterTest extends DummyFactory {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("관리자가 아닌 사용자가 접근하면 403을 반환한다.")
    void authorization_admin_fail_test() throws Exception {
        //given
        User user = newUser("jae@naver.com", "jaehyeon1114", "1234");

        User savedUser = userRepository.save(user);
        String jwtToken = JwtProvider.create(savedUser);
        System.out.println("테스트 : " + jwtToken);

        //when
        ResultActions resultActions = mvc.perform(get("/api/admin/hello/test").header(JwtVO.HEADER, jwtToken));

        //then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자가 접근하면 200을 반환한다.")
    void authorization_admin_success_test() throws Exception {
        //given
        User admin = newUser("jae@naver.com", "jaehyeon1114", "1234");
        PrincipalDetails loginAdmin = new PrincipalDetails(admin);
        String jwtToken = JwtProvider.create(admin);
        System.out.println("테스트 : " + jwtToken);

        //when
        ResultActions resultActions = mvc.perform(get("/api/admin/hello/test").header(JwtVO.HEADER, jwtToken));

        //then
        resultActions.andExpect(status().isForbidden());
    }
}