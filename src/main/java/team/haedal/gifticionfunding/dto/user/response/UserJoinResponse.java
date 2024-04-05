package team.haedal.gifticionfunding.dto.user.response;

import java.time.LocalDateTime;
import team.haedal.gifticionfunding.entity.user.User;

public record UserJoinResponse(
        Long id,
        String email,
        LocalDateTime createdAt
) {
    public static UserJoinResponse of(User user) {
        return new UserJoinResponse(user.getId(), user.getEmail(), user.getCreatedDate());
    }
}
