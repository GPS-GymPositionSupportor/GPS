package gps.base.rdb.error.exception;


import gps.base.rdb.error.ErrorCode;
import gps.base.rdb.error.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(
            CustomException e,
            HttpServletRequest request) {
        log.error("CustomException : {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();

        // 심각한 오류인 경우 에러 페이지로 리다이렉트
        if(isCriticalError(errorCode)) {
            return handleCriticalError(errorCode, request);
        }

        // 일반적인 오류는 JSON 응답
        return handleNormalError(errorCode, request);

    }

    private boolean isCriticalError(ErrorCode errorCode) {
        return errorCode == ErrorCode.INTERNAL_SERVER_ERROR ||
                errorCode == ErrorCode.SERVICE_UNAVAILABLE ||
                errorCode == ErrorCode.DATABASE_ERROR ||
                errorCode == ErrorCode.EXTERNAL_API_ERROR ||
                errorCode == ErrorCode.SECURITY_ERROR ||
                errorCode == ErrorCode.AUTHENTICATION_ERROR ||
                errorCode == ErrorCode.AUTHORIZATION_ERROR;
    }

    private ResponseEntity<ErrorResponse> handleCriticalError(ErrorCode errorCode, HttpServletRequest request) {
        log.error("Critical error occurred: {}", errorCode);

        // 에러 로깅 강화
        log.error("Request URL: {}", request.getRequestURL());
        log.error("Request Method: {}", request.getMethod());
        log.error("Client IP: {}", request.getRemoteAddr());

        HttpHeaders headers = new HttpHeaders();

        // 에러 타입에 따른 리다이렉트 처리
        switch (errorCode) {
            case INTERNAL_SERVER_ERROR:
            case DATABASE_ERROR:
                headers.setLocation(URI.create("/error/500"));
                break;
            case SERVICE_UNAVAILABLE:
                headers.setLocation(URI.create("/error/503"));
                break;
            case SECURITY_ERROR:
            case AUTHENTICATION_ERROR:
            case AUTHORIZATION_ERROR:
                headers.setLocation(URI.create("/error/security"));
                break;
            default:
                headers.setLocation(URI.create("/error/500"));
        }

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }



    private ResponseEntity<ErrorResponse> handleNormalError(ErrorCode errorCode, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }




}
