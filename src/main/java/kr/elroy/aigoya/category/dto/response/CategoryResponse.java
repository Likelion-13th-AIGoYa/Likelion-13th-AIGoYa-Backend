package kr.elroy.aigoya.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.category.domain.Category;

@Schema(description = "카테고리 응답")
public record CategoryResponse(
        @Schema(description = "카테고리 ID", example = "1")
        Long id,

        @Schema(description = "카테고리 이름", example = "찌개")
        String name
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
