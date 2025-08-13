package kr.elroy.aigoya.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "주문 수정 요청")
public record UpdateOrderRequest(
        @Schema(description = "주문 상품 목록")
        @NotEmpty
        @Valid
        List<OrderProductRequest> orderProducts
) {
    @Schema(description = "주문 상품 정보")
    public record OrderProductRequest(
            @Schema(description = "상품 ID", example = "1")
            @NotNull
            Long productId,

            @Schema(description = "주문 수량", example = "2")
            @NotNull
            Integer quantity
    ) {}
}