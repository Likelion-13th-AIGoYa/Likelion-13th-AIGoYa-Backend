package kr.elroy.aigoya.order;

import kr.elroy.aigoya.order.dto.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.OrderResponse;
import kr.elroy.aigoya.order.dto.UpdateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @Override
    public OrderResponse getOrder(Long id) {
        return orderService.getOrder(id);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @Override
    public OrderResponse updateOrder(Long id, UpdateOrderRequest request) {
        return orderService.updateOrder(id, request);
    }

    @Override
    public void deleteOrder(Long id) {
        orderService.deleteOrder(id);
    }
}