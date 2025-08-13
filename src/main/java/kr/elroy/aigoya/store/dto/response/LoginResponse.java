package kr.elroy.aigoya.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponse(
        @Schema(description = "JWT 액세스 토큰")
        String accessToken,

        @Schema(description = "토큰 타입", example = "Bearer")
        String tokenType,

        @Schema(description = "가게 ID")
        Long storeId,

        @Schema(description = "가게명")
        String storeName,

        @Schema(description = "이메일")
        String email
) {
    public static LoginResponse of(String accessToken, Long storeId, String storeName, String email) {
        return new LoginResponse(accessToken, "Bearer", storeId, storeName, email);
    }
}