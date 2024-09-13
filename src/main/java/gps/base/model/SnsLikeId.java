package gps.base.model;

import java.io.Serializable;
import java.util.Objects;

// 복합 Key 를 위한 ID 클래스
public class SnsLikeId implements Serializable {

    private Long user;
    private Long gym;

    // 기본 생성자

    public SnsLikeId() {
    }

    public SnsLikeId(Long user, Long gym) {
        this.user = user;
        this.gym = gym;
    }


    // Method


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SnsLikeId that = (SnsLikeId) o;
        return Objects.equals(user, that.user) && Objects.equals(gym, that.gym);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, gym);
    }
}
