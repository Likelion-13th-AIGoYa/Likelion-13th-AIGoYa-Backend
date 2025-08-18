package kr.elroy.aigoya.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.order.Order;
import kr.elroy.aigoya.product.dto.response.ProductResponse;

import java.util.List;

@Schema(description = "주문 응답")
public record OrderResponse(
        @Schema(description = "주문 ID", example = "1")
        Long orderId,

        @Schema(description = "가게 ID", example = "100")
        Long storeId,

        @Schema(description = "가게 이름", example ="K-Cafe")
        String storeName,

        @Schema(description = "주문 상품 목록")
        List<OrderProductResponse> orderProducts,

        @Schema(description = "주문 총 금액", example = "25000")
        Long totalPrice
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
                order.getStore().getId(),
                order.getStore().getName(),
                orderProducts,
                order.getTotalPrice()
        );
    }
}