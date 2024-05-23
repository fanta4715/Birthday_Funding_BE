package team.haedal.gifticionfunding.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import team.haedal.gifticionfunding.entity.funding.FundingArticle;

public record FundingArticleDto(
        String author,
        String birthdate,
        String title,
        String content,
        String endAt,
        List<String> gificonImageUrls
) {
    public static FundingArticleDto fromEntity(FundingArticle fundingArticle) {
        return new FundingArticleDto(
                fundingArticle.getAuthor().getNickname(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(fundingArticle.getAuthor().getBirthdate()),
                fundingArticle.getTitle(),
                fundingArticle.getContent(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(fundingArticle.getEndAt()),
                fundingArticle.getGifticons().stream()
                        .map(g -> g.getGifticon().getImageUrl())
                        .toList()
        );


    }
}
