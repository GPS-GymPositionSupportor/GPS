package gps.base.repository;

import gps.base.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {

    // 체육관 이름으로 검색 (부분 일치)
    List<Gym> findByGNameContaining(String gName);

    // 특정 관리자가 등록한 체육관 목록 조회
    List<Gym> findByAdminAdminId(Long adminId);

    // 특정 위치 근처의 체육관 검색 (위도와 경도 범위 내)
    List<Gym> findByGLatitudeBetweenAndGLongitudeBetween(Double minLatitude, Double maxLatitude, Double minLongitude, Double maxLongitude);


    // 평점 순으로 체육관 정렬
    List<Gym> findAllByOrderByRatinDesc();

    // 특정 평점 이상의 체육관 검색
    List<Gym> findByRatingGreaterThanEqual(double rating);

    // 특정 날짜 이후에 생성된 체육관 조회
    List<Gym> findByGCreatedAtAfter(LocalDateTime createdAt);

    // 주소로 체육관 검색 (부분 일치)
    List<Gym> findByAddressContaining(String address);

    // 체육관 이름과 주소로 검색 (둘 다 부분 일치)
    List<Gym> findByGNameContainingAndAddressContaining(String gName, String address);
}
