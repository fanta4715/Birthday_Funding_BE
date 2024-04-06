package team.haedal.gifticionfunding.entity.gifticon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.haedal.gifticionfunding.entity.gifticon.Gifticon;
import team.haedal.gifticionfunding.entity.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserGifticon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    private LocalDate usedDate;

    @Column(length = 20)
    private String giftCode;

    @Builder
    private UserGifticon(User buyer, User owner, Gifticon gifticon, LocalDate purchaseDate, LocalDate expirationDate, LocalDate usedDate, String giftCode) {
        this.buyer = buyer;
        this.owner = owner;
        this.gifticon = gifticon;
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
        this.usedDate = usedDate;
        this.giftCode = giftCode;
    }
}
