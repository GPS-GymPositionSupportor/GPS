package gps.base.ElasticError;

// 위치 범위 초과 예외
public class LocationOutOfBoundsException extends ElasticSearchException {
    public LocationOutOfBoundsException() {
        super(ElasticErrorCode.DISTANCE_CALCULATION_ERROR);
    }

    public LocationOutOfBoundsException(String message) {
        super(ElasticErrorCode.DISTANCE_CALCULATION_ERROR, message);
    }

    public LocationOutOfBoundsException(String message, Throwable cause) {
        super(ElasticErrorCode.DISTANCE_CALCULATION_ERROR, message, cause);
    }
}
