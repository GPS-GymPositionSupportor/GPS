package gps.base.rdb.model;

import java.io.Serializable;
import java.util.Objects;

// 복합 Key를 위한 클래스
public class FavoriteId implements Serializable {

    private Long user;
    private Long gym;

    // Constructor
    public FavoriteId() {
    }

    public FavoriteId(Long user, Long gym) {
        this.user = user;
        this.gym = gym;
    }


    // method


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(user, that.user) && Objects.equals(gym, that.gym);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, gym);
    }
}
