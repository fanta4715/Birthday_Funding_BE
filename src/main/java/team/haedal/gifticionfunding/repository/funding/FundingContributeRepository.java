package team.haedal.gifticionfunding.repository.funding;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.haedal.gifticionfunding.entity.funding.FundingContribute;

public interface FundingContributeRepository extends JpaRepository<FundingContribute, Long> {

    @Query(
            "SELECT fc.fundingArticleGifticon.id as fundingArticleGifticonId, "
                    + "COUNT(fc) as contributerNumber " +
            "FROM FundingContribute fc " +
            "WHERE fc.fundingArticleGifticon.id IN :fundingArticleGifticonIds " +
            "GROUP BY fc.fundingArticleGifticon.id"
    )
    List<FundingContributerNumber> countByFundingArticleGifticonIdIn(
            @Param("fundingArticleGifticonIds") List<Long> fundingArticleGifticonIds
    );

    @Query(
            "SELECT fc.fundingArticleGifticon.id as fundingArticleGifticonId, "
                    + "SUM("
                    + "CASE WHEN fc.point IS NULL THEN fc.userGifticon.gifticon.price "
                    + "WHEN fc.userGifticon IS NULL THEN fc.point END"
                    + ") as contributeAmount " +
            "FROM FundingContribute fc " +
            "WHERE fc.fundingArticleGifticon.id IN :fundingArticleGifticonIds " +
            "GROUP BY fc.fundingArticleGifticon.id"
    )
    List<FundingContributeAmount> sumByFundingArticleGifticonIdIn(
            @Param("fundingArticleGifticonIds") List<Long> fundingArticleGifticonIds
    );

    interface FundingContributerNumber {
        Long getFundingArticleGifticonId();
        Integer getContributerNumber();
    }

    interface FundingContributeAmount {
        Long getFundingArticleGifticonId();
        Integer getContributeAmount();
    }
}
