package kr.elroy.aigoya.exception;

public enum ErrorCode {
    ACCESS_DENIED(403, "AUTH_0001", "접근 권한이 없습니다."),

    ORDER_NOT_FOUND(404, "ORDER_0001", "주문을 찾을 수 없습니다."),

    PRODUCT_NOT_FOUND(404, "PRODUCT_0001", "상품을 찾을 수 없습니다."),

    STORE_NOT_FOUND(404, "STORE_0001", "가게를 찾을 수 없습니다."),

    ;
    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
