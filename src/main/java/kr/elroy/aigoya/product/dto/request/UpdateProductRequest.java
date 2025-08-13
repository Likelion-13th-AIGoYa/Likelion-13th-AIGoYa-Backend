package kr.elroy.aigoya.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "상품 수정 요청")
public record UpdateProductRequest(
        @Schema(description = "상품 이름", example = "치킨")
        @NotBlank
        String name,

        @Schema(description = "상품 가격", example = "18000")
        @NotNull
        @Positive
        Long price
) {
}