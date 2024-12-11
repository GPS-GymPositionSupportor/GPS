package gps.base.search.ElasticError.Handler;

import gps.base.ElasticError.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Global Exception Handler
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ElasticSearchConnectionException.class)
    public ResponseEntity<ErrorResponse> handleElasticSearchConnectionException(
            ElasticSearchConnectionException e,
            jakarta.servlet.http.HttpServletRequest request) {
        log.error("ElasticSearch 연결 오류: ", e);
        ErrorResponse response = ErrorResponse.of(
                e.getErrorCode(),
                request.getRequestURI(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(GymNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGymNotFoundException(
            GymNotFoundException e,
            jakarta.servlet.http.HttpServletRequest request) {
        log.error("체육관 검색 결과 없음: ", e);
        ErrorResponse response = ErrorResponse.of(
                e.getErrorCode(),
                request.getRequestURI(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(InvalidSearchParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSearchParameterException(
            InvalidSearchParameterException e,
            jakarta.servlet.http.HttpServletRequest request) {
        log.error("잘못된 검색 매개변수: ", e);
        ErrorResponse response = ErrorResponse.of(
                e.getErrorCode(),
                request.getRequestURI(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(LocationOutOfBoundsException.class)
    public ResponseEntity<ErrorResponse> handleLocationOutOfBoundsException(
            LocationOutOfBoundsException e,
            jakarta.servlet.http.HttpServletRequest request) {
        log.error("위치 범위 초과: ", e);
        ErrorResponse response = ErrorResponse.of(
                e.getErrorCode(),
                request.getRequestURI(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

    // 기타 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception e,
            jakarta.servlet.http.HttpServletRequest request) {
        log.error("예상치 못한 오류 발생: ", e);
        ErrorResponse response = ErrorResponse.of(
                ElasticErrorCode.ELASTICSEARCH_CONNECTION_ERROR, // 기본 에러 코드
                request.getRequestURI(),
                "내부 서버 오류가 발생했습니다."
        );
        return new ResponseEntity<>(response, ElasticErrorCode.ELASTICSEARCH_CONNECTION_ERROR.getHttpStatus());
    }
}
