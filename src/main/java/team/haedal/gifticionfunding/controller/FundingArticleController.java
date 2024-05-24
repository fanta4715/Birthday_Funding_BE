package team.haedal.gifticionfunding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.haedal.gifticionfunding.annotation.UserId;
import team.haedal.gifticionfunding.dto.FundingArticleDetailDto;
import team.haedal.gifticionfunding.dto.FundingArticleDto;
import team.haedal.gifticionfunding.dto.common.PagingResponse;
import team.haedal.gifticionfunding.dto.common.ResponseDto;
import team.haedal.gifticionfunding.service.FundingArticleService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FundingArticleController {
    private final FundingArticleService fundingArticleService;

    @GetMapping("/v1/fundings/articles")
    public ResponseEntity<?> getFundingArticles(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        PagingResponse<FundingArticleDto> fundingArticleDtoPage = fundingArticleService.getFundingArticles(userId, page, size);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "펀딩 게시글 목록 조회 성공", fundingArticleDtoPage),
                HttpStatus.OK
        );
    }

    @GetMapping("/v1/fundings/articles/{articleId}")
    public ResponseEntity<?> getFundingArticle(
            @UserId Long userId,
            @RequestParam(value = "articleId") Long articleId
    ) {
        FundingArticleDetailDto fundingArticleDto = fundingArticleService.getFundingArticle(userId, articleId);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "펀딩 게시글 조회 성공", fundingArticleDto),
                HttpStatus.OK
        );
    }

    @PatchMapping("/v1/fundings/articles/{articleId}/expiration")
    public ResponseEntity<?> updateFundingArticleExpiration(
            @UserId Long userId,
            @RequestParam(value = "articleId") Long articleId
    ) {
        fundingArticleService.updateFundingArticleExpiration(userId, articleId);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "펀딩 게시글 연장 성공", null),
                HttpStatus.OK
        );
    }

    @PostMapping("/v1/fundings/articles/gifticons/{fundingArticleGifticonId}/success")
    public ResponseEntity<?> receiveFunding(
            @UserId Long userId,
            @RequestParam(value = "fundingArticleGifticonId") Long fundingArticleGifticonId
    ) {
        fundingArticleService.receiveFunding(userId, fundingArticleGifticonId);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "펀딩 게시글 성공 처리 성공", null),
                HttpStatus.OK
        );
    }
}
