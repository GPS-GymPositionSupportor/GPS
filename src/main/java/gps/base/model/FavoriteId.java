package gps.base.model;

import java.io.Serializable;
import java.util.Objects;

// 복합 Key를 위한 클래스
public class FavoriteId implements Serializable {

    private Long userId;
    private Long gymId;

    // Constructor
    public FavoriteId() {
    }

    public FavoriteId(Long userId, Long gymId) {
        this.userId = userId;
        this.gymId = gymId;
    }


    // method


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(gymId, that.gymId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, gymId);
    }
}