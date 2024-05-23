package team.haedal.gifticionfunding.entity.funding;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.haedal.gifticionfunding.entity.common.BaseTimeEntity;
import team.haedal.gifticionfunding.entity.gifticon.UserGifticon;
import team.haedal.gifticionfunding.entity.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FundingContribute extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User contributor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_article_giftcion_id")
    private FundingArticleGifticon fundingArticleGifticon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_gifticon_id")
    private UserGifticon userGifticon;

    @Builder
    private FundingContribute(Long point, User contributor, FundingArticleGifticon fundingArticleGifticon, UserGifticon userGifticon) {
        this.point = point;
        this.contributor = contributor;
        this.fundingArticleGifticon = fundingArticleGifticon;
        this.userGifticon = userGifticon;
    }
}
