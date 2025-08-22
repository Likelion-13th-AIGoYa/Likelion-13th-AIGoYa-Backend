package kr.elroy.aigoya.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum ErrorCode {
    ACCESS_DENIED(403, "AUTH_0001", "접근 권한이 없습니다."),

    INVALID_ANALYSIS_PARAMETER(400, "ANALYTICS_0001", "유효하지 않은 분석 파라미터입니다."),

    CATEGORY_NOT_FOUND(404, "CATEGORY_0001", "카테고리를 찾을 수 없습니다."),
    CATEGORY_NAME_ALREADY_EXISTS(400, "CATEGORY_0002", "이미 사용 중인 카테고리 이름입니다."),
    CATEGORY_IN_USE(400, "CATEGORY_0003", "상품이 등록된 카테고리는 삭제할 수 없습니다."),

    ORDER_NOT_FOUND(404, "ORDER_0001", "주문을 찾을 수 없습니다."),

    PRODUCT_NOT_FOUND(404, "PRODUCT_0001", "상품을 찾을 수 없습니다."),

    STORE_NOT_FOUND(404, "STORE_0001", "가게를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXIST(400, "STORE_0002", "이미 존재하는 이메일입니다."),
    INVALID_EMAIL_OR_PASSWORD(401, "STORE_0003", "이메일 또는 비밀번호가 일치하지 않습니다."),
    CURRENT_PASSWORD_MISMATCH(400, "STORE_0004", "현재 비밀번호가 일치하지 않습니다."),
    SAME_PASSWORD_NOT_ALLOWED(400, "STORE_0005", "새 비밀번호는 현재 비밀번호와 달라야 합니다."),


    EMPLOYEE_NOT_FOUND(404, "EMPLOYEE_0001", "직원을 찾을 수 없습니다."),

    NOTIFICATION_NOT_FOUND(404, "NOTI_0001", "알림을 찾을 수 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
