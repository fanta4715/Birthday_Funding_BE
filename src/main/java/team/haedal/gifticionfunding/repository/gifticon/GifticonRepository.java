package team.haedal.gifticionfunding.repository.gifticon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.haedal.gifticionfunding.entity.gifticon.Gifticon;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
    @Query("SELECT g FROM Gifticon g WHERE " +
            "(:search is null or g.name like :search%) " +
            "and (:category is null or g.category = :category) ")
    Page<Gifticon> findAllWithFiltersAndSearch(
            @Param("search") String search,
            @Param("category") String category,
            Pageable pageable
    );
}
