package gps.base.repository;

import gps.base.ElasticSearchEntity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
    Optional<Gym> findByName(String name);

    @Query("SELECT g FROM Gym g WHERE g.category = :category")
    List<Gym> findAllByCategory(String category);

    @Query("SELECT g FROM Gym g WHERE " +
            "6371 * acos(cos(radians(:latitude)) * cos(radians(g.latitude)) * " +
            "cos(radians(g.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(g.latitude))) < :distance")
    List<Gym> findNearbyGyms(double latitude, double longitude, double distance);
}