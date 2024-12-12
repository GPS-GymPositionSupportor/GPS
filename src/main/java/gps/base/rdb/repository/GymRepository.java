package gps.base.rdb.repository;

import gps.base.rdb.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {

    // 체육관 이름으로 검색 (부분 일치)
    List<Gym> findBygNameContaining(String gName);

    // 평점 순으로 체육관 정렬
    List<Gym> findAllByOrderByRatingDesc();

    // 특정 평점 이상의 체육관 검색
    List<Gym> findByRatingGreaterThanEqual(double rating);

    // 특정 날짜 이후에 생성된 체육관 조회
    List<Gym> findBygCreatedAtAfter(LocalDateTime createdAt);

    // 주소로 체육관 검색 (부분 일치)
    List<Gym> findByAddressContaining(String address);

    // 체육관 이름과 주소로 검색 (둘 다 부분 일치)
    List<Gym> findBygNameContainingAndAddressContaining(String gName, String address);

    @Query(value = "SELECT g FROM Gym g WHERE " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(g.gLatitude)) * " +
            "cos(radians(g.gLongitude) - radians(:userLng)) + " +
            "sin(radians(:userLat)) * sin(radians(g.gLatitude)))) < 3")
    List<Gym> findGymsWithin3Km(@Param("userLat") double userLat,
                                @Param("userLng") double userLng);
}
