package team.haedal.gifticionfunding.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EFundingArticleStatus {
    PROCESSING("PROCESSING"),
    FINISH("FINISH"),
    ;

    private final String value;
}
