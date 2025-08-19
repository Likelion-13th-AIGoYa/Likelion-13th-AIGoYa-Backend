package kr.elroy.aigoya.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.product.domain.Product;

@Schema(description = "상품 응답 DTO")
public record ProductResponse(
        @Schema(description = "상품 ID", example = "1")
        Long productId,

        @Schema(description = "상품 이름", example = "치킨")
        String productName,

        @Schema(description = "상품 가격", example = "18000")
        Long price
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
