package team.haedal.gifticionfunding.entity.gifticon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Gifticon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long price;


    @Column(nullable = false)
    private String name;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GifticonCategory category;

    @Column(nullable = false)
    private Long stock;

    private String imageUrl;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreBrand brand;

    @Column(nullable = false)
    private Integer expirationPeriod;

    @Builder
    private Gifticon(Long price, String name, GifticonCategory category, Long stock, String imageUrl, String description, StoreBrand brand, Integer expirationPeriod) {
        this.price = price;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.description = description;
        this.brand = brand;
        this.expirationPeriod = expirationPeriod;
    }
}
