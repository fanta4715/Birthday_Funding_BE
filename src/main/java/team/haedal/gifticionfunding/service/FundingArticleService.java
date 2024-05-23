package team.haedal.gifticionfunding.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.haedal.gifticionfunding.dto.FundingArticleDto;
import team.haedal.gifticionfunding.dto.common.PagingResponse;
import team.haedal.gifticionfunding.entity.funding.FundingArticle;
import team.haedal.gifticionfunding.repository.funding.FundingArticleRepository;

@Service
@RequiredArgsConstructor
public class FundingArticleService {
    private final FundingArticleRepository fundingArticleRepository;

    /**
     * 친구 depth2 범위의 펀딩 게시글을 목록 조회한다.
     * @param page
     * @param size
     * @return
     */
    @Transactional(readOnly = true)
    public PagingResponse<FundingArticleDto> getFundingArticles(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("endAt").ascending());

        // 친구 depth2 범위의 게시글 조회
        Page<FundingArticle> fundingArticlePage = fundingArticleRepository.findAllWithAuthorAndGifticonsByFriendOfFriend(userId, pageable);

        // FundingArticleDto로 변환
        List<FundingArticleDto> fundingArticleDtoList = fundingArticlePage.getContent().stream()
                .map(FundingArticleDto::fromEntity)
                .toList();

        return PagingResponse.<FundingArticleDto>builder()
                .hasNext(fundingArticlePage.hasNext())
                .data(fundingArticleDtoList)
                .build();
    }
}
