package gps.base.ElasticError;

// 연결 관련 예외
public class ElasticSearchConnectionException extends ElasticSearchException {
    public ElasticSearchConnectionException() {
        super(ElasticErrorCode.ELASTICSEARCH_CONNECTION_ERROR);
    }

    public ElasticSearchConnectionException(String message) {
        super(ElasticErrorCode.ELASTICSEARCH_CONNECTION_ERROR, message);
    }

    public ElasticSearchConnectionException(String message, Throwable cause) {
        super(ElasticErrorCode.ELASTICSEARCH_CONNECTION_ERROR, message, cause);
    }
}
