package team.haedal.gifticionfunding.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import team.haedal.gifticionfunding.dto.common.JwtTokensDto;
import team.haedal.gifticionfunding.security.oauth.PrincipalDetails;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.entity.user.UserRole;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // 토큰 생성
    public JwtTokensDto generateAccessToken(Long userId, UserRole role) {
        Claims claims = Jwts.claims();

        claims.put("userId", userId.toString());
        claims.put("role", role.getName());

        Date expityDate = Date.from(
                Instant.now()
                        .plus(JwtVO.ACCESS_TOKEN_EXPIRATION_TIME, ChronoUnit.MILLIS));

        String accessToken = Jwts.builder().signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setClaims(claims)
                .setIssuer("Gifticion Funding")
                .setIssuedAt(new Date())
                .setExpiration(expityDate)
                .compact();

        return JwtTokensDto.builder()
                .accessToken(accessToken)
                .build();
    }

    // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // JWT 토큰에서 사용자 정보를 추출하는 메서드
    public Claims extractClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
