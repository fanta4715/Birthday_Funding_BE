package team.haedal.gifticionfunding.dto;

import java.util.List;
import java.util.Map;
import team.haedal.gifticionfunding.entity.funding.FundingArticle;

public record FundingArticleDetailDto(
        String author,
        String birthdate,
        String title,
        String content,
        String endAt,
        List<FundingArticleGifticonDto> goalGifticons
) {

    public static FundingArticleDetailDto of(
            FundingArticle fundingArticle,
            Map<Long, Integer> fundAmountMap,
            Map<Long, Integer> numberOfSupportersMap
    ) {
        return new FundingArticleDetailDto(
                fundingArticle.getAuthor().getNickname(),
                fundingArticle.getAuthor().getBirthdate().toString(),
                fundingArticle.getTitle(),
                fundingArticle.getContent(),
                fundingArticle.getEndAt().toString(),
                fundingArticle.getGifticons().stream()
                        .map(fundingArticleGifticon -> FundingArticleGifticonDto.of(
                                        fundingArticleGifticon,
                                        fundAmountMap.getOrDefault(fundingArticleGifticon.getId(), 0),
                                        numberOfSupportersMap.getOrDefault(fundingArticleGifticon.getId(), 0)
                                )
                        )
                        .toList()
        );
    }
}
