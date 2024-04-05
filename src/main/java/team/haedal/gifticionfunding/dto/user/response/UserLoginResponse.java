package team.haedal.gifticionfunding.dto.user.response;

public record UserLoginResponse(
        String accessToken,
        Long id
) {
}
