package team.haedal.gifticionfunding.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ROLE_USER("USER", "ROLE_USER"),
    ROLE_ADMIN("ADMIN", "ROLE_ADMIN");

    private final String name;
    private final String securityName;
}
