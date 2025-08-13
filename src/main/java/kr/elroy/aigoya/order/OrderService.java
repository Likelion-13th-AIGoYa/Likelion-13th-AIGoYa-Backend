package kr.elroy.aigoya.order;

import kr.elroy.aigoya.order.dto.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.OrderResponse;
import kr.elroy.aigoya.order.dto.UpdateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 ID입니다."));
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        // TODO: Implement order creation logic
        return null;
    }

    @Transactional
    public OrderResponse updateOrder(Long id, UpdateOrderRequest request) {
        // TODO: Implement order update logic
        return null;
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 ID입니다."));

        orderRepository.delete(order);
    }
}