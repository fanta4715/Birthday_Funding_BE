package team.haedal.gifticionfunding.controller.gifticon;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.haedal.gifticionfunding.annotation.UserId;
import team.haedal.gifticionfunding.security.oauth.PrincipalDetails;
import team.haedal.gifticionfunding.dto.common.PagingResponse;
import team.haedal.gifticionfunding.dto.common.ResponseDto;
import team.haedal.gifticionfunding.dto.gifticon.request.GifticonPurchaseRequest;
import team.haedal.gifticionfunding.dto.gifticon.response.GifticonDetailResponse;
import team.haedal.gifticionfunding.dto.gifticon.response.GifticonDto;
import team.haedal.gifticionfunding.dto.gifticon.response.UserGifticonDto;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.service.gifticon.GifticonService;

@RestController
@RequestMapping("/api/v1/gifticons")
@RequiredArgsConstructor
public class GifticonController {
    private final GifticonService gifticonService;

    @GetMapping("")
    public ResponseEntity<?> getGifticonPaging(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "criteria", defaultValue = "createdDate") String criteria,
            @RequestParam(value = "sort", defaultValue = "DESC") String sort
    ) {
        PagingResponse<GifticonDto> gifticonPaging = gifticonService.getGifticonPaging(page, size, search, criteria, sort, category);

        return new ResponseEntity<>(
                new ResponseDto<>(1, "기프티콘 목록 조회 성공", gifticonPaging),
                HttpStatus.OK
        );
    }

    @GetMapping("/{gifticonId}")
    public ResponseEntity<?> getGifticonDetail(
            @PathVariable Long gifticonId
    ) {
        GifticonDetailResponse gifticonDetail = gifticonService.getGifticonDetail(gifticonId);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "기프티콘 상세 조회 성공", gifticonDetail),
                HttpStatus.OK
        );
    }

    @PostMapping("/purchase/{gifticonId}")
    public ResponseEntity<?> purchaseGifticon(
            @PathVariable Long gifticonId,
            @RequestBody GifticonPurchaseRequest gifticonPurchaseRequest,
            @UserId Long userId
    ) {
        List<UserGifticonDto> purchasedGifticons = gifticonService.purchaseGifticon(gifticonId, gifticonPurchaseRequest, userId);

        return new ResponseEntity<>(
                new ResponseDto<>(1, "기프티콘 구매 성공", purchasedGifticons),
                HttpStatus.CREATED
        );
    }


}
