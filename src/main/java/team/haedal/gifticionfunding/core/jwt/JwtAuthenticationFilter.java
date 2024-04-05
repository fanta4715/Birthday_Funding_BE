package team.haedal.gifticionfunding.core.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import team.haedal.gifticionfunding.core.security.auth.PrincipalDetails;
import team.haedal.gifticionfunding.dto.user.request.UserLoginRequest;
import team.haedal.gifticionfunding.dto.user.response.UserLoginResponse;
import team.haedal.gifticionfunding.util.CustomResponseUtil;

/**
 * OAuth가 아닌 일반 로그인시 사용하는 필터 POST /api/login 요청시 동작한다 로그인을 시도하고 JWT 토큰을 생성하여 응답한다
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }

    // Post : /api/login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            ObjectMapper om = new ObjectMapper();
            UserLoginRequest loginRequest = om.readValue(request.getInputStream(), UserLoginRequest.class);

            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.email(), loginRequest.password());

            // 내부적으로 loadUserByUsername 함수가 실행되고, 세션도 실행됨
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return authentication;
        } catch (Exception e) {
            // unsuccessfulAuthentication 호출함
            throw new InternalAuthenticationServiceException(e.getMessage());
        }

    }

    /**
     * 로그인 실패시 동작하는 함수
     */
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) {
        CustomResponseUtil.fail(response, "로그인 실패", HttpStatus.UNAUTHORIZED);
    }

    /**
     * 로그인 성공시 동작하는 함수 JWT 토큰을 생성하여 반환한다.
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) {
        PrincipalDetails loginUser = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = JwtProvider.create(loginUser.getUser());
        response.addHeader(JwtVO.HEADER, jwtToken);

        UserLoginResponse loginRespDto = new UserLoginResponse(jwtToken, loginUser.getUser().getId());
        CustomResponseUtil.success(response, loginRespDto);
    }
}
