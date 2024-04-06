package team.haedal.gifticionfunding.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import team.haedal.gifticionfunding.entity.gifticon.UserGifticon;

public interface UserGifticonRepository extends JpaRepository<UserGifticon, Long> {
}
