package kr.elroy.aigoya.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.order.Order;

// TODO: Implement OrderResponse with necessary fields and methods
@Schema(description = "주문 응답")
public record OrderResponse (

) {
    public static OrderResponse from(Order order) {
        return null;
    }
}