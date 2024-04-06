package team.haedal.gifticionfunding.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequest(
        @NotNull @Email @Length(max = 50)
        String email,

        @NotNull @Length(min = 8, max = 50)
        String password
) {
}
