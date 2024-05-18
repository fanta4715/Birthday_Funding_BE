package team.haedal.gifticionfunding.dto;

import java.time.format.DateTimeFormatter;
import lombok.Builder;
import team.haedal.gifticionfunding.entity.user.User;

@Builder
public record UserDto (
        Long id,
        String name,
        String birthdate,
        String profileImageUrl
){
    public static UserDto fromEntity(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return UserDto.builder()
                .id(user.getId())
                .birthdate(user.getBirthdate().format(formatter))
                .profileImageUrl(user.getProfileImageUrl())
                .name(user.getNickname())
                .build();
    }
}
