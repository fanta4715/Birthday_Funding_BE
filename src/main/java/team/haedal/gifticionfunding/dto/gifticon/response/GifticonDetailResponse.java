package team.haedal.gifticionfunding.dto.gifticon.response;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import team.haedal.gifticionfunding.entity.gifticon.Gifticon;
import team.haedal.gifticionfunding.entity.gifticon.GifticonCategory;
import team.haedal.gifticionfunding.entity.gifticon.StoreBrand;

public record GifticonDetailResponse(
        Long id,
        String name,
        int price,
        GifticonCategory category,
        StoreBrand brand,
        String imageUrl,
        String description,
        Integer expirationPeriod
) {
    public static GifticonDetailResponse from(Gifticon gifticon) {
        return new GifticonDetailResponse(
                gifticon.getId(),
                gifticon.getName(),
                gifticon.getPrice().intValue(),
                gifticon.getCategory(),
                gifticon.getBrand(),
                gifticon.getImageUrl(),
                gifticon.getDescription(),
                gifticon.getExpirationPeriod()
        );
    }
}
