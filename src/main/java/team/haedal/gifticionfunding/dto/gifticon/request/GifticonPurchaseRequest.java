package team.haedal.gifticionfunding.dto.gifticon.request;

import jakarta.validation.constraints.NotNull;

public record GifticonPurchaseRequest(
        @NotNull
        int quantity
) {
}
