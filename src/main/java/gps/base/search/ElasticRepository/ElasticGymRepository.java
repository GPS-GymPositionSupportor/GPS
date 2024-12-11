package gps.base.search.ElasticRepository;


import gps.base.search.ElasticEntity.ElasticGym;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticGymRepository extends JpaRepository<ElasticGym, Long> {
    @Query("SELECT g FROM Gym g WHERE g.gymId IN :ids")
    Page<ElasticGym> searchByIds(List<Long> ids, Pageable pageable);

    List<ElasticGym> findAllById(Iterable<Long> ids);

    // 수정된 쿼리 메서드 - 파라미터 이름을 일치시킴
    @Query("SELECT g FROM Gym g WHERE g.gymId IN :ids")
    List<ElasticGym> findByIdIn(@Param("ids") List<Long> ids);

    // 페이징을 위한 메서드
    @Query("SELECT g FROM Gym g WHERE g.gymId IN :ids")
    Page<ElasticGym> findByIdIn(@Param("ids") List<Long> ids, Pageable pageable);
}