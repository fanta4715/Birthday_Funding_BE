package team.haedal.gifticionfunding.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.haedal.gifticionfunding.dto.FundingArticleDetailDto;
import team.haedal.gifticionfunding.dto.FundingArticleDto;
import team.haedal.gifticionfunding.dto.common.PagingResponse;
import team.haedal.gifticionfunding.entity.funding.FundingArticle;
import team.haedal.gifticionfunding.entity.funding.FundingArticleGifticon;
import team.haedal.gifticionfunding.entity.gifticon.UserGifticon;
import team.haedal.gifticionfunding.entity.type.EFundingArticleGifticonStatus;
import team.haedal.gifticionfunding.entity.type.EFundingArticleStatus;
import team.haedal.gifticionfunding.repository.funding.FundingArticleGifticonRepository;
import team.haedal.gifticionfunding.repository.funding.FundingArticleRepository;
import team.haedal.gifticionfunding.repository.funding.FundingContributeRepository;
import team.haedal.gifticionfunding.repository.funding.FundingContributeRepository.FundingContributeAmount;
import team.haedal.gifticionfunding.repository.funding.FundingContributeRepository.FundingContributerNumber;
import team.haedal.gifticionfunding.repository.user.FriendshipRepository;
import team.haedal.gifticionfunding.repository.user.UserGifticonRepository;

@Service
@RequiredArgsConstructor
public class FundingArticleService {
    private static final int MAX_EXTENSION_DATE = 3;

    private final FundingArticleRepository fundingArticleRepository;
    private final FundingContributeRepository fundingContributeRepository;
    private final FundingArticleGifticonRepository fundingArticleGifticonRepository;
    private final UserGifticonRepository userGifticonRepository;
    private final FriendshipRepository friendshipRepository;
    /**
     * 친구 depth2 범위의 펀딩 게시글을 목록 조회한다.
     *
     * @param page
     * @param size
     * @return
     */
    @Transactional(readOnly = true)
    public PagingResponse<FundingArticleDto> getFundingArticles(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("endAt").ascending());

        // 친구 depth2 범위의 게시글 조회
        Page<FundingArticle> fundingArticlePage = fundingArticleRepository.findAllWithAuthorAndGifticonsByFriendOfFriend(
                userId, pageable);

        // FundingArticleDto로 변환
        List<FundingArticleDto> fundingArticleDtoList = fundingArticlePage.getContent().stream()
                .map(FundingArticleDto::fromEntity)
                .toList();

        return PagingResponse.<FundingArticleDto>builder()
                .hasNext(fundingArticlePage.hasNext())
                .data(fundingArticleDtoList)
                .build();
    }

    /**
     * 펀딩 게시글 상세 조회. 해당 게시글의 후원 금액과 후원자 수를 함께 조회한다.
     *
     * @param userId
     * @param articleId
     * @return
     */
    @Transactional(readOnly = true)
    public FundingArticleDetailDto getFundingArticle(Long userId, Long articleId) {
        // 펀딩 게시글 조회
        FundingArticle fundingArticle = fundingArticleRepository.findAllWithAuthorAndGifticonsById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 펀딩 게시글이 존재하지 않습니다."));

        Long authorId = fundingArticle.getAuthor().getId();
        if (!friendshipRepository.existsUserInFriendsOfFriends(userId, authorId)) {
            throw new IllegalArgumentException("친구 depth 2 범위의 펀딩 게시글이 아닙니다.");
        }

        List<Long> fundingArticleGifticonIds = fundingArticle.getGifticons().stream()
                .map(fundingArticleGifticon -> fundingArticleGifticon.getId())
                .toList();

        // 해당 게시글 기프티콘당 성취 금액 조회
        List<FundingContributeAmount> fundingContributerAmounts = fundingContributeRepository
                .sumByFundingArticleGifticonIdIn(fundingArticleGifticonIds);

        Map<Long, Integer> fundAmountMap = fundingContributerAmounts.stream()
                .collect(
                        Collectors.toMap(
                                FundingContributeAmount::getFundingArticleGifticonId,
                                FundingContributeAmount::getContributeAmount
                        )
                );

        // 해당 게시글 기프티콘당 후원자 수 조회
        List<FundingContributerNumber> fundingContributerNumbers = fundingContributeRepository
                .countByFundingArticleGifticonIdIn(fundingArticleGifticonIds);

        Map<Long, Integer> numberOfSupportersMap = fundingContributerNumbers.stream()
                .collect(
                        Collectors.toMap(
                                FundingContributerNumber::getFundingArticleGifticonId,
                                FundingContributerNumber::getContributerNumber
                        )
                );

        return FundingArticleDetailDto.of(fundingArticle, fundAmountMap, numberOfSupportersMap);
    }

    @Transactional
    public void updateFundingArticleExpiration(Long userId, Long articleId) {
        FundingArticle fundingArticle = fundingArticleRepository.findWithAuthorById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 펀딩 게시글이 존재하지 않습니다."));

        if (!fundingArticle.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 펀딩 게시글의 작성자가 아닙니다.");
        }

        fundingArticle.updateExpiration(MAX_EXTENSION_DATE);
    }

    @Transactional
    public void receiveFunding(Long userId, Long fundingArticleGifticonId) {
        FundingArticleGifticon fundingArticleGifticon = fundingArticleGifticonRepository.findWithArticleAndAuthorById(fundingArticleGifticonId)
                .orElseThrow(() -> new IllegalArgumentException("해당 펀딩 게시글 기프티콘이 존재하지 않습니다."));

        if (!fundingArticleGifticon.getFundingArticle().getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 펀딩 게시글의 작성자가 아닙니다.");
        }

        // userGifticon 생성
        UserGifticon receivedGifticon = UserGifticon.builder()
                .buyer(fundingArticleGifticon.getFundingArticle().getAuthor())
                .owner(fundingArticleGifticon.getFundingArticle().getAuthor())
                .gifticon(fundingArticleGifticon.getGifticon())
                .build();

        // fundingArticleGifticon 상태 변경
        fundingArticleGifticon.updateStatus(EFundingArticleGifticonStatus.FINISH_SUCCESS);

        // 마지막 후원까지 종료되었을 경우, 펀딩 게시글 상태 변경
        int numberOfReceivedGifticons = (int) fundingArticleGifticon.getFundingArticle().getGifticons().stream()
                .filter(fundingArticleGifticon1 -> fundingArticleGifticon1.getStatus().equals(EFundingArticleGifticonStatus.FINISH_SUCCESS))
                .count();
        if (numberOfReceivedGifticons == fundingArticleGifticon.getFundingArticle().getGifticons().size()) {
            fundingArticleGifticon.getFundingArticle().updateStatus(EFundingArticleStatus.FINISH);
        }
        // userGifticon 저장 및 flush
        userGifticonRepository.saveAndFlush(receivedGifticon);
    }
}
