package kr.elroy.aigoya.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "주문 수정 요청")
public record UpdateOrderRequest(
        @Schema(description = "주문 상품 목록")
        @NotEmpty
        @Valid
        List<OrderProductDto> orderProducts
) {
}