package team.haedal.gifticionfunding.service.oauth;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import team.haedal.gifticionfunding.dto.common.JwtTokensDto;
import team.haedal.gifticionfunding.dto.oauth.KakaoUserInfo;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.repository.user.UserRepository;
import team.haedal.gifticionfunding.security.jwt.JwtProvider;
import org.springframework.web.reactive.function.client.WebClient;
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthKakaoService {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;
    private final JwtProvider jwtProvider;
    private final WebClient webClientForGetAccessToken = WebClient.builder()
            .baseUrl("https://kauth.kakao.com/oauth/token")
            .build();

    private final WebClient webClientForGetUserInfo = WebClient.builder()
            .baseUrl("https://kapi.kakao.com/v2/user/me")
            .build();
    private final UserRepository userRepository;

    public String getAccessToken(String authorizationCode) {
        Map<String, String> responseBody;
        try {
            responseBody = webClientForGetAccessToken.post()
                    .uri("")
                    .headers(httpHeaders -> {
                        httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                    })
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", CLIENT_ID)
                            .with("client_secret", CLIENT_SECRET)
                            .with("code", authorizationCode))
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, String>>() {
                    })
                    .block().getBody();

        } catch (Exception e) {
            //TODO: 예외 처리
            throw new RuntimeException("카카오 서버 토큰 발급 에러");
        }

        return responseBody.get("access_token").toString();
    }

    public KakaoUserInfo getUserInfoByAccessToken(String accessToken) {
        Map<String, Object> responseBody;

        try {
            responseBody = webClientForGetUserInfo.post()
                    .uri("")
                    .headers(httpHeaders -> {
                        httpHeaders.set("Authorization", "Bearer " + accessToken);
                        httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                    })
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block().getBody();
        } catch (Exception e) {
            log.info("error: {}", e.getMessage());
            throw new RuntimeException("카카오 서버 유저 정보 조회 에러");
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
        Map<String, String> profile = (Map<String, String>) kakaoAccount.get("profile");

        log.info("카카오 유저 정보 조회 성공");
        log.info("responseBody: {}", responseBody);

        return KakaoUserInfo.builder()
                .providerId(responseBody.get("id").toString())
                .nickname(profile.get("nickname"))
                .profileImageUrl(profile.get("thumbnail_image_url").toString())
                .email(kakaoAccount.get("email").toString())
                .build();
    }

    public JwtTokensDto loginOrRegister(KakaoUserInfo userInfo) {
        //유저가 존재하는지 확인
        Optional<User> user = userRepository.findByEmail(userInfo.email());
        if (user.isEmpty()) {
            //유저가 존재하지 않으면 회원가입
            User newUser = userInfo.toEntity();
            User savedUser = userRepository.save(newUser);
            return jwtProvider.generateAccessToken(
                    savedUser.getId(),
                    savedUser.getRole()
            );
        }

        return jwtProvider.generateAccessToken(
                user.get().getId(),
                user.get().getRole()
        );
    }
}
