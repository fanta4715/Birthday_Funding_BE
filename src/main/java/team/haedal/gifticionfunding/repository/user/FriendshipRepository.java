package team.haedal.gifticionfunding.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.haedal.gifticionfunding.entity.user.Friendship;
import team.haedal.gifticionfunding.entity.user.User;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT f.toUser FROM Friendship f WHERE f.fromUser.id = :userId")
    Page<User> findFriendsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE u.id IN (" +
            "    SELECT f.toUser.id FROM Friendship f WHERE f.fromUser.id = :userId " + // cl
            "    OR f.toUser.id IN (" +
            "        SELECT f2.toUser.id FROM Friendship f1 JOIN Friendship f2 ON f1.toUser.id = f2.fromUser.id WHERE f1.fromUser.id = :userId" +
            "    )" +
            ")")
    Page<User> findFriendsAndFriendsOfFriends(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u " +
            "WHERE u.id = :targetUserId AND u.id IN (" +
            "    SELECT f2.toUser.id FROM Friendship f1 " +
            "    JOIN Friendship f2 ON f1.toUser.id = f2.fromUser.id " +
            "    WHERE f1.fromUser.id = :userId)")
    Boolean existsUserInFriendsOfFriends(
            @Param("userId") Long userId,
            @Param("targetUserId") Long targetUserId
    );
}
