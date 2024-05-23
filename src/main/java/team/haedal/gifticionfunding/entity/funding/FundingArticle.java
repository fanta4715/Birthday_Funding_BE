package team.haedal.gifticionfunding.entity.funding;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import team.haedal.gifticionfunding.entity.common.BaseTimeEntity;
import team.haedal.gifticionfunding.entity.type.EFundingArticleStatus;
import team.haedal.gifticionfunding.entity.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicUpdate
public class FundingArticle extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User author;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EFundingArticleStatus status;

    @OneToMany(mappedBy = "fundingArticle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FundingArticleGifticon> gifticons = new ArrayList<>();
    @Builder
    private FundingArticle(User author, String title, String content, LocalDateTime endAt) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.endAt = endAt;

        this.status = EFundingArticleStatus.PROCESSING;
    }

    public void updateExpiration(int maxExtensionDate) {
        this.endAt = this.endAt.plusDays(maxExtensionDate);
    }

    public void updateStatus(EFundingArticleStatus status) {
        this.status = status;
    }
}
