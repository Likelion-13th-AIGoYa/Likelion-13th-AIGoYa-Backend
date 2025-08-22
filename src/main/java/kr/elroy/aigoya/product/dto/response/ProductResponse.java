package kr.elroy.aigoya.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.category.domain.Category;
import kr.elroy.aigoya.product.domain.Product;

@Schema(description = "상품 응답 DTO")
public record ProductResponse(
        @Schema(description = "상품 ID", example = "1")
        Long productId,

        @Schema(description = "상품 이름", example = "치킨")
        String productName,

        @Schema(description = "상품 가격", example = "18000")
        Long price,

        @Schema(description = "카테고리 정보")
        CategoryInfo category
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                CategoryInfo.from(product.getCategory())
        );
    }

    @Schema(description = "카테고리 정보")
    public record CategoryInfo(
            @Schema(description = "카테고리 ID", example = "1")
            Long id,
            @Schema(description = "카테고리 이름", example = "메인 메뉴")
            String name
    ) {
        public static CategoryInfo from(Category category) {
            return new CategoryInfo(category.getId(), category.getName());
        }
    }
}
