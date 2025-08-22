package kr.elroy.aigoya.category.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "카테고리 수정 요청")
public record UpdateCategoryRequest(
        @Schema(description = "카테고리 이름", example = "튀김")
        @NotBlank(message = "카테고리 이름은 필수입니다.")
        String name
) {
}
