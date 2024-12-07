package gps.base.repository;

import gps.base.model.Favorite;
import gps.base.model.FavoriteId;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, FavoriteId> {

    @Query("SELECT f FROM Favorite f WHERE f.user.userId = :userId")
    List<Favorite> findByUserId(@Param("userId") Long userId);
}
