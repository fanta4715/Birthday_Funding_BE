package team.haedal.gifticionfunding.core.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import team.haedal.gifticionfunding.core.jwt.JwtProvider;
import team.haedal.gifticionfunding.core.jwt.JwtVO;
import team.haedal.gifticionfunding.core.security.auth.PrincipalDetails;
import team.haedal.gifticionfunding.dto.user.response.UserLoginResponse;
import team.haedal.gifticionfunding.util.CustomResponseUtil;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        PrincipalDetails loginUser = (PrincipalDetails) authentication.getPrincipal();
        String jwtToken = JwtProvider.create(loginUser.getUser());
        response.addHeader(JwtVO.HEADER, jwtToken);

        UserLoginResponse loginRespDto = new UserLoginResponse(jwtToken, loginUser.getUser().getId());
        CustomResponseUtil.success(response, loginRespDto);
    }
}
