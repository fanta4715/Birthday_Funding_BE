package team.haedal.gifticionfunding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.haedal.gifticionfunding.entity.funding.FundingArticleGifticon;

@Getter
public record FundingArticleGifticonDto(
        String gifticonName,
        Integer price,
        String category,
        String imageUrl,
        Integer achievementRate,
        Integer currentFundAmount,
        Integer numberOfSupporters
) {

    public static FundingArticleGifticonDto of(FundingArticleGifticon fundingArticleGifticon, Integer currentFundAmount, Integer numberOfSupporters) {
        int achievementRate = (int) ((double) currentFundAmount / fundingArticleGifticon.getGifticon().getPrice() * 100);
        int price = fundingArticleGifticon.getGifticon().getPrice().intValue();

        return new FundingArticleGifticonDto(
                fundingArticleGifticon.getGifticon().getName(),
                price,
                fundingArticleGifticon.getGifticon().getCategory().toString(),
                fundingArticleGifticon.getGifticon().getImageUrl(),
                achievementRate,
                currentFundAmount,
                numberOfSupporters
        );
    }
}
