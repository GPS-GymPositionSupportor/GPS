package gps.base.ElasticError;

// 기본 예외 클래스
public class ElasticSearchException extends RuntimeException {
    private final ElasticErrorCode errorCode;

    public ElasticSearchException(ElasticErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ElasticSearchException(ElasticErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ElasticSearchException(ElasticErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ElasticErrorCode getErrorCode() {
        return errorCode;
    }
}

