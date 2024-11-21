package gps.base.ElasticError.DTO;

import gps.base.ElasticError.ElasticErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

// 에러 응답 DTO
@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final String code;
    private final String message;
    private final String path;
    private final String errorDetail;

    // 정적 팩토리 메소드
    public static ErrorResponse of(ElasticErrorCode errorCode, String path, String errorDetail) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getErrorCode())
                .message(errorCode.getMessage())
                .path(path)
                .errorDetail(errorDetail)
                .build();
    }
}

