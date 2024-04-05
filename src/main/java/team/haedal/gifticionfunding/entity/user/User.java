package team.haedal.gifticionfunding.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.haedal.gifticionfunding.entity.common.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false)
    private Long point;

    @Column(nullable = false)
    private LocalDate birthdate;

    private String profileImageUrl;

    // OAuth를 위해 구성한 추가 필드 2개
    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    @Builder
    private User(String email, String password, String nickname, Long point, LocalDate birthdate, String profileImageUrl,
            String provider, String providerId, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.point = point;
        this.birthdate = birthdate;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
        this.providerId = providerId;
        this.role = userRole;
    }

    public User(Long id, UserRole role) {
        this.id = id;
        this.role = role;
    }
}
