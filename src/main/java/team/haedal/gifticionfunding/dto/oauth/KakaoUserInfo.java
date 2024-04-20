package team.haedal.gifticionfunding.dto.oauth;

import java.time.LocalDate;
import lombok.Builder;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.entity.user.UserRole;
@Builder
public record KakaoUserInfo(
        String providerId,
        String email,
        String nickname,
        String profileImageUrl
) {
    public User toEntity() {
        return User.builder()
                .provider("kakao")
                .providerId(providerId)
                .email(email)
                .nickname(nickname)
                .point(0L)
                .birthdate(LocalDate.of(2000, 11, 14))
                .profileImageUrl(profileImageUrl)
                .role(UserRole.ROLE_USER)
                .build();
    }
}
