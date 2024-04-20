package team.haedal.gifticionfunding.dto.common;

import lombok.Builder;

@Builder
public record JwtTokensDto(
        String accessToken //TODO: 추후 refresh token 구현하기
) {
}
