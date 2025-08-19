package kr.elroy.aigoya.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.exception.ErrorCode;

@Schema(description = "에러 응답")
public record ErrorResponse(
        @Schema(description = "에러 코드", example = "AUTH_0001")
        String code,

        @Schema(description = "에러 메시지", example = "접근 권한이 없습니다.")
        String message
) {
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}