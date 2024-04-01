package team.haedal.gifticionfunding.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import team.haedal.gifticionfunding.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
