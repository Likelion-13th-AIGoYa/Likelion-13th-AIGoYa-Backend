package kr.elroy.aigoya.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "가게 생성 요청")
public record CreateStoreRequest(
        @Email
        @Schema(description = "가게 이메일", example = "me@elroy.kr")
        String email,

        @Schema(description = "가게 이름", example = "Elroy Store")
        @NotBlank
        String name,

        @Schema(description = "가게 비밀번호", example = "password123")
        @NotBlank
        String password
) {
}
