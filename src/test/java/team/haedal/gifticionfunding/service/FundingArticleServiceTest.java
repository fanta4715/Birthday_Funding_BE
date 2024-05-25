package team.haedal.gifticionfunding.service;


import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.haedal.gifticionfunding.dto.FundingArticleDetailDto;
import team.haedal.gifticionfunding.dto.FundingArticleDto;
import team.haedal.gifticionfunding.dto.common.PagingResponse;
import team.haedal.gifticionfunding.dummy.DummyFactory;
import team.haedal.gifticionfunding.entity.funding.FundingArticle;
import team.haedal.gifticionfunding.entity.user.Friendship;
import team.haedal.gifticionfunding.entity.user.User;
import team.haedal.gifticionfunding.repository.funding.FundingArticleRepository;
import team.haedal.gifticionfunding.repository.user.FriendshipRepository;
import team.haedal.gifticionfunding.repository.user.UserRepository;

@SpringBootTest
class FundingArticleServiceTest extends DummyFactory {
    @Autowired private FundingArticleService fundingArticleService;
    @Autowired private UserRepository userRepository;
    @Autowired private FriendshipRepository friendshipRepository;
    @Autowired private FundingArticleRepository fundingArticleRepository;

    @AfterEach
    void tearDown() {
        fundingArticleRepository.deleteAllInBatch();
        friendshipRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("존재하지 않는 펀딩 게시글을 조회하면, IllegalArgumentException 예외가 발생한다.")
    void throw_when_read_non_existed_article() {
        //given
        Long nonExistentId = 1L;

        //when & then
        assertThrows(IllegalArgumentException.class, () -> fundingArticleService.getFundingArticle(1L, nonExistentId));
    }

    @Test
    @DisplayName("친구 depth 2 범위가 아닌 펀딩 게시글을 조회하면, IllegalArgumentException 예외가 발생한다.")
    void throw_when_read_out_of_depth2() {
        //given
        //유저 생성
        User user = newUser("jae@naver.com", "jaehyeon1114", "1234");
        User depth1Friend = newUser("jae2@naver.com", "jaehyeon1115", "1234");
        User depth2Friend = newUser("jae3@naver.com", "jaehyeon1116", "1234");
        User depth3Friend = newUser("jae4@naver.com", "jaehyeon1117", "1234");

        userRepository.saveAll(List.of(user, depth1Friend, depth2Friend, depth3Friend));

        //친구관계 생성
        Friendship friendship1 = Friendship.builder().fromUser(user).toUser(depth1Friend).build();
        Friendship friendship2 = Friendship.builder().fromUser(depth1Friend).toUser(depth2Friend).build();
        Friendship friendship3 = Friendship.builder().fromUser(depth2Friend).toUser(depth3Friend).build();

        friendshipRepository.saveAll(List.of(friendship1, friendship2, friendship3));

        // depth3 친구 게시글 생성
        FundingArticle fundingArticle = FundingArticle.builder()
                .author(depth3Friend)
                .title("title")
                .content("content")
                .endAt(now().plusDays(1).atStartOfDay())
                .build();

        fundingArticleRepository.save(fundingArticle);

        //when & then
        assertThrows(IllegalArgumentException.class, () -> fundingArticleService.getFundingArticle(user.getId(), fundingArticle.getId()));
    }

    @Test
    @DisplayName("친구 depth 2 범위의 펀딩 게시글을 정상적으로 조회한다.")
    void success_when_read_article_in_depth2() {
        //given
        //유저 생성
        User user = newUser("jae@naver.com", "jaehyeon1114", "1234");
        User depth1Friend = newUser("jae2@naver.com", "jaehyeon1115", "1234");
        User depth2Friend = newUser("jae3@naver.com", "jaehyeon1116", "1234");

        userRepository.saveAll(List.of(user, depth1Friend, depth2Friend));

        //친구관계 생성
        Friendship friendship1 = Friendship.builder().fromUser(user).toUser(depth1Friend).build();
        Friendship friendship2 = Friendship.builder().fromUser(depth1Friend).toUser(depth2Friend).build();

        friendshipRepository.saveAll(List.of(friendship1, friendship2));

        // depth3 친구 게시글 생성
        FundingArticle fundingArticle = FundingArticle.builder()
                .author(depth2Friend)
                .title("title")
                .content("content")
                .endAt(now().plusDays(1).atStartOfDay())
                .build();

        fundingArticleRepository.save(fundingArticle);

        //when
        FundingArticleDetailDto fundingArticleDetailDto = fundingArticleService.getFundingArticle(user.getId(), fundingArticle.getId());

        //then
        assertEquals(fundingArticle.getAuthor().getNickname(), fundingArticleDetailDto.author());
    }
    @Test
    @DisplayName("친구 depth 2 범위의 펀딩 게시글 목록을 정상적으로 조회한다.")
    void success_when_read_articles_in_depth2() {
        //유저 생성
        User user = newUser("jae@naver.com", "jaehyeon1114", "1234");
        User depth1Friend = newUser("jae2@naver.com", "jaehyeon1115", "1234");
        User depth2Friend = newUser("jae3@naver.com", "jaehyeon1116", "1234");
        User depth3Friend = newUser("jae4@naver.com", "jaehyeon1117", "1234");

        userRepository.saveAll(List.of(user, depth1Friend, depth2Friend, depth3Friend));

        //친구관계 생성
        Friendship friendship1 = Friendship.builder().fromUser(user).toUser(depth1Friend).build();
        Friendship friendship2 = Friendship.builder().fromUser(depth1Friend).toUser(depth2Friend).build();
        Friendship friendship3 = Friendship.builder().fromUser(depth2Friend).toUser(depth3Friend).build();

        friendshipRepository.saveAll(List.of(friendship1, friendship2, friendship3));

        // depth1 친구 게시글 생성
        FundingArticle fundingArticle1 = FundingArticle.builder()
                .author(depth1Friend)
                .title("title")
                .content("content")
                .endAt(now().plusDays(1).atStartOfDay())
                .build();

        // depth2 친구 게시글 생성
        FundingArticle fundingArticle2 = FundingArticle.builder()
                .author(depth2Friend)
                .title("title")
                .content("content")
                .endAt(now().plusDays(1).atStartOfDay())
                .build();

        // depth3 친구 게시글 생성
        FundingArticle fundingArticle3 = FundingArticle.builder()
                .author(depth3Friend)
                .title("title")
                .content("content")
                .endAt(now().plusDays(1).atStartOfDay())
                .build();

        fundingArticleRepository.saveAll(List.of(fundingArticle1, fundingArticle2, fundingArticle3));

        //when
        PagingResponse<FundingArticleDto> fundingArticleDtos = fundingArticleService.getFundingArticles(user.getId(), 0, 10);

        //then
        assertEquals(2, fundingArticleDtos.getData().size());
    }

    @Test
    void updateFundingArticleExpiration() {
    }

    @Test
    void receiveFunding() {
    }
}