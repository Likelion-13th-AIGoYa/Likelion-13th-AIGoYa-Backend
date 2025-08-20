package kr.elroy.aigoya.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "비밀번호 변경 요청")
public record UpdatePasswordRequest(
        @NotBlank(message = "현재 비밀번호를 입력해주세요.")
        @Schema(description = "현재 사용 중인 비밀번호", example = "password123")
        String currentPassword,

        @NotBlank(message = "새 비밀번호를 입력해주세요.")
        @Size(min = 8, message = "새 비밀번호는 8자 이상이어야 합니다.")
        @Schema(description = "새로 사용할 비밀번호 (8자 이상)", example = "newPassword1234")
        String newPassword
) {
}