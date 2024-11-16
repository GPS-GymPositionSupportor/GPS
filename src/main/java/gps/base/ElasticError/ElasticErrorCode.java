package gps.base.ElasticError;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ElasticErrorCode {
    // ElasticSearch 연결 관련 오류 (200번대)
    ELASTICSEARCH_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "200", "ElasticSearch 연결에 실패했습니다."),
    ELASTICSEARCH_QUERY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "201", "검색 쿼리 실행 중 오류가 발생했습니다."),
    ELASTICSEARCH_SYNC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "202", "데이터 동기화 중 오류가 발생했습니다."),

    // 검색 관련 오류 (210번대)
    INVALID_SEARCH_PARAMETER(HttpStatus.BAD_REQUEST, "210", "검색 매개변수가 올바르지 않습니다."),
    SEARCH_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "211", "검색 결과가 없습니다."),
    DISTANCE_CALCULATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "212", "거리 계산 중 오류가 발생했습니다."),

    // 추천 시스템 관련 오류 (220번대)
    RECOMMENDATION_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "220", "추천 처리 중 오류가 발생했습니다."),
    INSUFFICIENT_RECOMMENDATION_DATA(HttpStatus.BAD_REQUEST, "221", "추천을 위한 데이터가 부족합니다."),
    SIMILARITY_CALCULATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "222", "유사도 계산 중 오류가 발생했습니다."),

    // 인덱스 관련 오류 (230번대)
    INDEX_NOT_FOUND(HttpStatus.NOT_FOUND, "230", "요청한 인덱스를 찾을 수 없습니다."),
    INDEX_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "231", "인덱스 생성 중 오류가 발생했습니다."),
    MAPPING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "232", "인덱스 매핑 중 오류가 발생했습니다."),

    // 데이터 동기화 관련 오류 (240번대)
    SYNC_DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "240", "동기화할 문서를 찾을 수 없습니다."),
    SYNC_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "241", "문서 동기화 중 오류가 발생했습니다."),
    BULK_OPERATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "242", "대량 작업 처리 중 오류가 발생했습니다.");

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
}