package team.haedal.gifticionfunding.core.security.oauth;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.haedal.gifticionfunding.core.security.auth.PrincipalDetails;
import team.haedal.gifticionfunding.core.security.oauth.provider.KakaoUserInfo;
import team.haedal.gifticionfunding.core.security.oauth.provider.OAuth2UserInfo;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.entity.user.UserRole;
import team.haedal.gifticionfunding.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // userRequest 는 code를 받아서 accessToken을 응답 받은 객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
        OAuth2UserInfo oAuth2UserInfo = null;

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (registrationId.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(attributes);
        } else {
            // TODO : Exception
            System.out.println("카카오만 가능");
        }

        String userEmail = oAuth2UserInfo.getEmail();
        Optional<User> userOP = userRepository.findByEmail(userEmail);
        User user;
        if (userOP.isEmpty()) {
            user = User.builder()
                    .email(userEmail)
                    .nickname(oAuth2UserInfo.getName())
                    .point(0L)
                    .birthdate(LocalDate.now()) //TODO : API KEY 권한 생기면 OAuth로 받아오도록 변경하기
                    .userRole(UserRole.ROLE_USER)
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            userRepository.save(user);
        } else {
            user = userOP.get();
        }
        // code를 통해 구성한 정보
        System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
        // token을 통해 응답받은 회원정보
        System.out.println("oAuth2User : " + oAuth2User);

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}