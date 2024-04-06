package team.haedal.gifticionfunding.dto.gifticon.response;

import team.haedal.gifticionfunding.entity.gifticon.Gifticon;
import team.haedal.gifticionfunding.entity.gifticon.GifticonCategory;
import team.haedal.gifticionfunding.entity.gifticon.StoreBrand;

public record GifticonDto(
        Long id,
        String name,
        Long price,
        String imageUrl,
        GifticonCategory category,
        StoreBrand brand
) {

    public static GifticonDto from(Gifticon gifticon) {
        return new GifticonDto(
                gifticon.getId(),
                gifticon.getName(),
                gifticon.getPrice(),
                gifticon.getImageUrl(),
                gifticon.getCategory(),
                gifticon.getBrand()
        );
    }
}
