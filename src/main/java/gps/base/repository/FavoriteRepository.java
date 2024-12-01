package gps.base.repository;

import gps.base.model.Favorite;
import gps.base.model.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    List<Favorite> findByUserIdUserId(Long userId);
}
