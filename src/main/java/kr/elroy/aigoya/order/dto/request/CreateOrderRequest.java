package kr.elroy.aigoya.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "주문 생성 요청")
public record CreateOrderRequest(
        @Schema(description = "주문 상품 목록")
        @NotEmpty
        @Valid
        List<OrderProductDto> orderProducts
) {
    @Schema(description = "주문 상품 정보")
    public record OrderProductDto(
            @Schema(description = "상품 ID", example = "1")
            @NotNull
            Long productId,

            @Schema(description = "주문 수량", example = "2")
            @NotNull
            Integer quantity
    ) {}
}