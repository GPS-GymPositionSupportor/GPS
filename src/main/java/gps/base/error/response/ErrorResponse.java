package gps.base.error.response;

import gps.base.error.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;   // HTTP 상태 코드
    private final String errorCode; // 커스텀 에러 코드
    private final String message;   // 에러 메시지
    private final String path;  // 에러 발생 경로
    
    
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .errorCode(errorCode.getErrorCode())
                .message(errorCode.getMessage())
                .path(path)
                .build();
    }
}
