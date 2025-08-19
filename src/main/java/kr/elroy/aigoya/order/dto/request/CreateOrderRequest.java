package kr.elroy.aigoya.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "주문 생성 요청")
public record CreateOrderRequest(
        @Schema(description = "주문 상품 목록")
        @NotEmpty
        @Valid
        List<OrderProductDto> orderProducts,

        @Schema(description = "주문 시간", example = "2023-10-01T12:00:00")
        @NotEmpty
        LocalDateTime orderedAt
) {
}