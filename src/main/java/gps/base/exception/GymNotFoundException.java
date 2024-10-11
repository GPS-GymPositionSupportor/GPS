package gps.base.exception;

public class GymNotFoundException extends RuntimeException {
    public GymNotFoundException(Long gymId) {
        super(gymId + " 에 해당하는 체육관을 찾을 수 없습니다.");
    }
}
