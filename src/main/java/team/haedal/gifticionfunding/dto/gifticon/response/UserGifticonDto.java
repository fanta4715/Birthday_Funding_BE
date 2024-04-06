package team.haedal.gifticionfunding.dto.gifticon.response;

import java.time.LocalDate;
import team.haedal.gifticionfunding.entity.gifticon.UserGifticon;

public record UserGifticonDto(
        Long id,
        String name,
        int price,
        String imageUrl,
        LocalDate purchaseDate,
        LocalDate expirationDate
) {
    public static UserGifticonDto from(UserGifticon userGifticon) {
        return new UserGifticonDto(
                userGifticon.getId(),
                userGifticon.getGifticon().getName(),
                userGifticon.getGifticon().getPrice().intValue(),
                userGifticon.getGifticon().getImageUrl(),
                userGifticon.getPurchaseDate(),
                userGifticon.getExpirationDate()
        );
    }
}
