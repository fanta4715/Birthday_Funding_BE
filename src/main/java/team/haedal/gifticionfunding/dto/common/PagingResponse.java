package team.haedal.gifticionfunding.dto.common;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagingResponse<T> {
    private boolean hasNext;
    private List<T> data;
}
