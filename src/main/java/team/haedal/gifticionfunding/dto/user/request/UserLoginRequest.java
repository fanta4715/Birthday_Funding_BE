package team.haedal.gifticionfunding.dto.user.request;

public record UserLoginRequest(
        String email,
        String password
) {
}
