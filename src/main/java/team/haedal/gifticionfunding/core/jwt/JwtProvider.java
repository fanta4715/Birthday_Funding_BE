package team.haedal.gifticionfunding.core.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import team.haedal.gifticionfunding.core.security.auth.PrincipalDetails;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.entity.user.UserRole;


public abstract class JwtProvider {
    // 토큰 생성
    public static String create(User user) {
        Date expityDate = Date.from(
                Instant.now()
                        .plus(JwtVO.ACCESS_TOKEN_EXPIRATION_TIME, ChronoUnit.MILLIS));

        String jwtToken = Jwts.builder().signWith(SignatureAlgorithm.HS512, JwtVO.SECRET)
                .setSubject(user.getId().toString()) //.claim("key",value)식으로 토큰에 설정 추가할 수 있음.
                .setIssuer("Gifticion Funding")
                .setIssuedAt(new Date())
                .claim("role",user.getRole())
                .setExpiration(expityDate)
                .compact();

        return JwtVO.TOKEN_PREFIX + jwtToken;
    }

    // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(JwtVO.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // JWT 토큰에서 사용자 정보를 추출하는 메서드
    public static PrincipalDetails extractUserDetailsFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JwtVO.SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long userId = Long.parseLong(claims.getSubject());
        UserRole role = UserRole.valueOf(claims.get("role").toString());

        User user = new User(userId, role);
        return new PrincipalDetails(user);
    }
}
