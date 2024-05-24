package team.haedal.gifticionfunding.dummy;


import java.time.LocalDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.entity.user.UserRole;

public class DummyFactory {

    protected static User newUser(String email, String nickname, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .point(0L)
                .role(UserRole.ROLE_USER)
                .nickname(nickname)
                .birthdate(LocalDate.of(1999, 1, 1))
                .build();
    }

    protected static User newAdmin(String email, String nickname, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .point(0L)
                .role(UserRole.ROLE_ADMIN)
                .nickname(nickname)
                .birthdate(LocalDate.of(1999, 1, 1))
                .build();
    }


}
