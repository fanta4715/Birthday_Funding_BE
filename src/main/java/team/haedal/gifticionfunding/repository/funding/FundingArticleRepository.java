package team.haedal.gifticionfunding.repository.funding;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.haedal.gifticionfunding.entity.funding.FundingArticle;

public interface FundingArticleRepository extends JpaRepository<FundingArticle, Long> {
    @Query(
            "SELECT fa FROM FundingArticle fa "
                    + "JOIN FETCH fa.author "
                    + "JOIN FETCH fa.gifticons "
            + "WHERE fa.author.id IN (" +
                    "    SELECT f.toUser.id FROM Friendship f " +
                    "WHERE f.fromUser.id = :userId " + // cl
                    "    OR f.toUser.id IN (" +
                    "        SELECT f2.toUser.id FROM Friendship f1 JOIN Friendship f2 ON f1.toUser.id = f2.fromUser.id WHERE f1.fromUser.id = :userId" +
                    "    )" +
                    ")"
    )
    Page<FundingArticle> findAllWithAuthorAndGifticonsByFriendOfFriend(
            @Param("userId") Long userId,
            Pageable pageable);

    @EntityGraph(attributePaths = {"author", "gifticons"})
    Optional<FundingArticle> findAllWithAuthorAndGifticonsById(Long articleId);

    @EntityGraph(attributePaths = {"author"})
    Optional<FundingArticle> findWithAuthorById(Long articleId);
}
