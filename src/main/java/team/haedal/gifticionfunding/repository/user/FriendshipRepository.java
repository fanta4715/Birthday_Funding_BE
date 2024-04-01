package team.haedal.gifticionfunding.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import team.haedal.gifticionfunding.entity.user.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
}
