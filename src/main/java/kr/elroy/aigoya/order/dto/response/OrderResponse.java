package kr.elroy.aigoya.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.order.domain.Order;
import kr.elroy.aigoya.product.dto.response.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "주문 응답")
public record OrderResponse(
        @Schema(description = "주문 ID", example = "1")
        Long orderId,

        @Schema(description = "주문 총 금액", example = "25000")
        Long totalPrice,

        @Schema(description = "주문 상품 목록")
        List<OrderProductResponse> orderProducts,

        @Schema(description = "주문 시각", example = "2024-08-22T15:30:00")
        LocalDateTime orderedAt
) {
    @Schema(description = "주문 상품 응답")
    public record OrderProductResponse(
            @Schema(description = "상품 정보")
            ProductResponse product,

            @Schema(description = "주문 수량", example = "2")
            Integer quantity
    ) {
    }

    public static OrderResponse from(Order order) {
        List<OrderProductResponse> orderProducts = order.getOrderProducts().stream()
                .map(orderProduct -> new OrderProductResponse(
                        ProductResponse.from(orderProduct.getProduct()),
                        orderProduct.getQuantity()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                orderProducts,
                order.getOrderedAt()
        );
    }
}
