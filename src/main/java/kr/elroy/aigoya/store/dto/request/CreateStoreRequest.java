package kr.elroy.aigoya.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "가게 생성 요청")
public record CreateStoreRequest(
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        @Schema(description = "가게 이메일 (로그인 ID)", example = "me@elroy.kr")
        String email,

        @NotBlank(message = "가게 이름은 필수입니다.")
        @Schema(description = "가게 이름", example = "Elroy Store")
        String name,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Schema(description = "가게 비밀번호", example = "password123")
        String password,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Schema(description = "전화번호", example = "010-1234-5678")
        String phone,

        @Schema(description = "가게 주소 (선택)", example = "서울시 강남구 테헤란로 123")
        String address,

        @Schema(description = "일일 목표 매출 (선택)", example = "1000000")
        Long dailyTarget
) {
}
