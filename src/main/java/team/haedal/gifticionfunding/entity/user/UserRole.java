package team.haedal.gifticionfunding.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ROLE_USER("USER"), ROLE_ADMIN("ADMIN");

    public final String value;
}
