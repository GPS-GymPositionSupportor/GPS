package gps.base.ElasticError;

// 체육관 찾기 실패 예외
public class GymNotFoundException extends ElasticSearchException {
    public GymNotFoundException() {
        super(ElasticErrorCode.SEARCH_RESULT_NOT_FOUND);
    }

    public GymNotFoundException(String message) {
        super(ElasticErrorCode.SEARCH_RESULT_NOT_FOUND, message);
    }

    public GymNotFoundException(String message, Throwable cause) {
        super(ElasticErrorCode.SEARCH_RESULT_NOT_FOUND, message, cause);
    }
}
