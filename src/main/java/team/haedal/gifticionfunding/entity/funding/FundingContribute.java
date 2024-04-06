package team.haedal.gifticionfunding.entity.funding;

import jakarta.persistence.Entity;
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
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.entity.gifticon.UserGifticon;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FundingContribute extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long point;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User contributor;

    @ManyToOne
    @JoinColumn(name = "funding_article_id")
    private FundingArticle fundingArticle;

    @ManyToOne
    @JoinColumn(name = "user_gifticon_id")
    private UserGifticon userGifticon;

    @Builder
    private FundingContribute(Long point, User contributor, FundingArticle fundingArticle, UserGifticon userGifticon) {
        this.point = point;
        this.contributor = contributor;
        this.fundingArticle = fundingArticle;
        this.userGifticon = userGifticon;
    }
}
