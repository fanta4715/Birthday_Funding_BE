package team.haedal.gifticionfunding.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EFundingArticleGifticonStatus {
    PROCESSING("PROCESSING"),
    FINISH_SUCCESS("FINISH_SUCCESS"),
    FINISH_FAIL("FINISH_FAIL"),
    ;

    private final String value;
}
