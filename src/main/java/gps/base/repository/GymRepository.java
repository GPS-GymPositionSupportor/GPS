package gps.base.repository;

import gps.base.ElasticSearchEntity.Gym;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
    @Query("SELECT g FROM Gym g WHERE g.id IN :ids")
    Page<Gym> searchByIds(List<Long> ids, Pageable pageable);

    Page<Gym> findByIdIn(List<Long> gymIds, Pageable pageable);
}