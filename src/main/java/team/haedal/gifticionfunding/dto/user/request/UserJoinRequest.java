package team.haedal.gifticionfunding.dto.user.request;

import java.time.LocalDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import team.haedal.gifticionfunding.entity.user.User;

public record UserJoinRequest(
        String email,
        String password,
        String nickname,
        LocalDate birthdate,
        MultipartFile profileImage
) {
    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .birthdate(birthdate)
                .build();
    }
}
