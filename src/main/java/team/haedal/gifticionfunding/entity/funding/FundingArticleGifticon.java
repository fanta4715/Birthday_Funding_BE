package team.haedal.gifticionfunding.entity.funding;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import team.haedal.gifticionfunding.entity.gifticon.Gifticon;
import team.haedal.gifticionfunding.entity.type.EFundingArticleGifticonStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FundingArticleGifticon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_id")
    private Gifticon gifticon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_article_id")
    private FundingArticle fundingArticle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EFundingArticleGifticonStatus status;

    @Builder
    private FundingArticleGifticon(Gifticon gifticon, FundingArticle fundingArticle) {
        this.gifticon = gifticon;
        this.fundingArticle = fundingArticle;
    }

    public void updateStatus(EFundingArticleGifticonStatus eFundingArticleGifticonStatus) {
        this.status = eFundingArticleGifticonStatus;
    }
}
