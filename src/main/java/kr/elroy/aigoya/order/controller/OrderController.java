package kr.elroy.aigoya.order.controller;

import kr.elroy.aigoya.order.OrderService;
import kr.elroy.aigoya.order.api.OrderApi;
import kr.elroy.aigoya.order.dto.request.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.request.UpdateOrderRequest;
import kr.elroy.aigoya.order.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @Override
    public OrderResponse getOrder(Long id, Long storeId) {
        return orderService.getOrder(id, storeId);
    }

    @Override
    public List<OrderResponse> getAllOrders(Long storeId) {
        if (storeId != null) {
            return orderService.getOrdersByStore(storeId); // 특정 가게 주문 조회
        }
        return orderService.getAllOrders(); // 전체 가게 주문 조회
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request, Long storeid) {
        return orderService.createOrder(request, storeid);
    }

    @Override
    public OrderResponse updateOrder(Long id, UpdateOrderRequest request, Long storeId) {
        return orderService.updateOrder(id, request, storeId);
    }

    @Override
    public void deleteOrder(Long id, Long storeId) {
        orderService.deleteOrder(id, storeId);
    }
}
