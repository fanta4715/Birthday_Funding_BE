package team.haedal.gifticionfunding.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.entity.user.UserRole;

public record UserJoinRequest(

        @NotNull @Email @Length(max = 50)
        String email,

        @NotNull @Length(min = 8, max = 50)
        String password,

        @NotNull @Length(max = 50)
        String nickname,

        @NotNull @Past
        LocalDate birthdate,

        MultipartFile profileImage
) {
    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .point(0L)
                .userRole(UserRole.ROLE_USER)
                .birthdate(birthdate)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .birthdate(birthdate)
                .build();
    }
}
