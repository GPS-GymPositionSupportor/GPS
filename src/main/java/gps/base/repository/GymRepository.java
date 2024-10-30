package gps.base.repository;

import gps.base.ElasticSearchEntity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface GymRepository extends JpaRepository<Gym, Long> {
    // 카테고리별 체육관 검색
    List<Gym> findByCategory(String category);

    // 위치 기반 체육관 검색
    @Query(value = "SELECT *, " +
            "ST_Distance_Sphere(point(g_longitude, g_latitude), point(?1, ?2)) as distance " +
            "FROM gym " +
            "HAVING distance <= ?3 " +
            "ORDER BY distance",
            nativeQuery = true)
    List<Gym> findNearbyGyms(double longitude, double latitude, double radiusInMeters);
}