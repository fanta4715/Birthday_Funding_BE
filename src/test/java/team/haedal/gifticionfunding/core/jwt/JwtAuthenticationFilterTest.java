package team.haedal.gifticionfunding.core.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import team.haedal.gifticionfunding.dto.user.request.UserLoginRequest;
import team.haedal.gifticionfunding.dummy.DummyFactory;
import team.haedal.gifticionfunding.repository.user.UserRepository;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyFactory {
    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        userRepository.save(newUser("jae@naver.com", "jaehyeon1114", "1234"));
    }

    @Test
    @DisplayName("로그인을 완료하면 헤더로 토큰을 리턴한다.")
    void successfulAuthentication_test() throws Exception {
        //given
        UserLoginRequest userLoginRequest = new UserLoginRequest(
                "jae@naver.com", "1234");
        String requestBody = om.writeValueAsString(userLoginRequest);
        System.out.println("테스트 : " + requestBody);

        //when
        ResultActions resultActions = mvc.perform(post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);
        System.out.println("테스트 : " + responseBody);
        System.out.println("테스트 : " + jwtToken);

        //then
        resultActions.andExpect(status().isOk());
        assertNotNull(jwtToken);
        assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("로그인에 실패하면 401을 리턴한다.")
    void test() {
        //given

        //when

        //then
    }
}