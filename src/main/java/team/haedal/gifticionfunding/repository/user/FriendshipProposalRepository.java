package team.haedal.gifticionfunding.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import team.haedal.gifticionfunding.entity.user.FriendshipProposal;

public interface FriendshipProposalRepository extends JpaRepository<FriendshipProposal, Long> {
}
