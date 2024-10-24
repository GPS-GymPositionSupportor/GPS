package gps.base.error;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "비즈니스 에러"),
    GYM_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "조회한 체육관이 존재하지 않습니다."),
    INVALID_GYM_ID(HttpStatus.BAD_REQUEST, "400", "[id] 도로명 주소 또는 체육관 이름이 불일치하여 데이터가 없는 ID 입니다. 다른 ID로 요청해주세요."),

    // 인증 관련
    NONE_TOKEN(HttpStatus.UNAUTHORIZED, "100", "HTTP Authorization header 에 토큰을 담아 요청해주세요."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "101", "유효하지 않은 토큰입니다."),
    EXPIRED_ATK(HttpStatus.UNAUTHORIZED, "102", "만료된 액세스 토큰입니다."),
    EXPIRED_RTK(HttpStatus.UNAUTHORIZED, "103", "만료된 리프레쉬 토큰입니다."),
    INVALID_RTK(HttpStatus.BAD_REQUEST, "104", "잘못된 리프레쉬 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "105", "로그인이 필요한 서비스입니다."),
    INVALID_REDIRECT_URI(HttpStatus.BAD_REQUEST, "105", "잘못된 Redirect URI 입니다."),


    DUPLICATE_FAVORITE(HttpStatus.CONFLICT, "409", "해당 체육관은 이미 스크랩 되어 있습니다."),
    DELETED_FAVORITE(HttpStatus.CONFLICT, "409", "해당 체육관은 이미 스크랩 되어있지 않습니다."),
    MEMBER_NICKNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 회원의 닉네임이 존재하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 회원을 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "리뷰를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "댓글을 찾을 수 없습니다."),
    WRITER_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "400", "작성자가 일치하지 않습니다."),
    DUPLICATE_MEMBER_NICKNAME(HttpStatus.CONFLICT, "409", "중복된 닉네임입니다."),
    TOO_MANY_REQUEST(HttpStatus.TOO_MANY_REQUESTS, "429", "일일 카카오 API 호출 한도가 초과되었습니다. 관리자에게 문의 바랍니다."),


    // Request Parameter, Body 관련 (0번대)
    INVALID_JSON(HttpStatus.BAD_REQUEST, "001", "Request Body JSON 형식이 잘못되었습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "002", "파라미터 형식이 잘못되었습니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "003", "필수 파라미터가 누락되었습니다."),

    // 파일 업로드
    NOT_IMAGE_FILE(HttpStatus.BAD_REQUEST, "004", "이미지 파일(PNG, Jpeg , gif, webp)이 아닙니다."),
    FILE_SIZE_EXCEED(HttpStatus.BAD_REQUEST, "005", "업로드 가능한 파일 용량을 초과했습니다."),
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "006", "이미지 업로드에 실패했습니다."),
    NO_FILE_PROVIDED(HttpStatus.BAD_REQUEST, "007", "요청에 제공된 파일이 없습니다."),



    // 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 내부 오류가 발생했습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "503", "서비스가 일시적으로 중단되었습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "501", "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "502", "외부 서비스 연동 중 오류가 발생했습니다."),

    // 심각한 보안 오류
    SECURITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "600", "보안 관련 오류가 발생했습니다."),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "601", "인증 처리 중 심각한 오류가 발생했습니다."),
    AUTHORIZATION_ERROR(HttpStatus.FORBIDDEN, "603", "권한 처리 중 심각한 오류가 발생했습니다.");




    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
}
