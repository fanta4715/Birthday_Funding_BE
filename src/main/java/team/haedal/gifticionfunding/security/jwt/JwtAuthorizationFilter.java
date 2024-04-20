package team.haedal.gifticionfunding.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.entity.user.UserRole;
import team.haedal.gifticionfunding.security.oauth.PrincipalDetails;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtProvider JwtProvider;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.JwtProvider = jwtProvider;
    }

    // JWT 토큰 헤더를 추가하지 않아도 해당 필터는 통과는 할 수 있지만, 결국 시큐리티단에서 세션 값 검증에 실패함.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (isHeaderVerify(request, response)) {
            String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PREFIX, "");
            JwtProvider.validateToken(token);
            log.info("토큰 검증 완료");
            Claims claims = JwtProvider.extractClaimsFromToken(token);
            String userIdStr = claims.get("userId", String.class);
            String roleStr = claims.get("role", String.class);
            PrincipalDetails userDetails = new PrincipalDetails(Long.parseLong(userIdStr), UserRole.of(roleStr));
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()); // id, role 만 존재

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtVO.HEADER);
        if (header == null || !header.startsWith(JwtVO.TOKEN_PREFIX)) {
            return false;
        } else {
            return true;
        }
    }
}
