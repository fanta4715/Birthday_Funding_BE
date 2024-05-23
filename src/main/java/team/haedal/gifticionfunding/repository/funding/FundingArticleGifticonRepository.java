package team.haedal.gifticionfunding.repository.funding;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.haedal.gifticionfunding.entity.funding.FundingArticleGifticon;

public interface FundingArticleGifticonRepository extends JpaRepository<FundingArticleGifticon, Long> {
    @Query("SELECT fag FROM FundingArticleGifticon fag "
            + "JOIN FETCH fag.fundingArticle "
            + "JOIN FETCH fag.fundingArticle.author "
            + "WHERE fag.id = :fundingArticleGifticonId")
    Optional<FundingArticleGifticon> findWithArticleAndAuthorById(
            @Param("fundingArticleGifticonId") Long fundingArticleGifticonId
    );
}
