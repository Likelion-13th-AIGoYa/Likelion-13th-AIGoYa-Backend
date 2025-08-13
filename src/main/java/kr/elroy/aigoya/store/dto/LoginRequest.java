package kr.elroy.aigoya.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "이메일", example = "me@elroy.kr", required = true)
        @NotBlank(message = "이메일은 필수입니다")
        String email,

        @Schema(description = "비밀번호", example = "password123", required = true)
        @NotBlank(message = "비밀번호는 필수입니다")
        String password
) {
}