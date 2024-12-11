package gps.base.search.ElasticError;

// 잘못된 검색 매개변수 예외
public class InvalidSearchParameterException extends ElasticSearchException {
    public InvalidSearchParameterException() {
        super(ElasticErrorCode.INVALID_SEARCH_PARAMETER);
    }

    public InvalidSearchParameterException(String message) {
        super(ElasticErrorCode.INVALID_SEARCH_PARAMETER, message);
    }

    public InvalidSearchParameterException(String message, Throwable cause) {
        super(ElasticErrorCode.INVALID_SEARCH_PARAMETER, message, cause);
    }
}
