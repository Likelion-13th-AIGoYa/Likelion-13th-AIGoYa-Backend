package kr.elroy.aigoya.order;

import kr.elroy.aigoya.order.dto.request.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.request.UpdateOrderRequest;
import kr.elroy.aigoya.order.dto.response.OrderResponse;
import kr.elroy.aigoya.product.OrderProduct;
import kr.elroy.aigoya.product.Product;
import kr.elroy.aigoya.product.ProductRepository;
import kr.elroy.aigoya.store.Store;
import kr.elroy.aigoya.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id, Long storeId) {
        Order order = orderRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 ID 혹은 가게 ID입니다."));
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStore(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new IllegalArgumentException("존재하지 않는 가게 ID입니다.");
        }
        return orderRepository.findByStoreId(storeId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게 ID입니다."));

        List<OrderProduct> orderProducts = request.orderProducts().stream()
                .map(productRequest -> {
                    Product product = productRepository.findById(productRequest.productId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID: " + productRequest.productId()));
                    return new OrderProduct(null, null, product, productRequest.quantity());
                }).collect(Collectors.toList());

        Order order = new Order(null, store, orderProducts, LocalDateTime.now(), calculateTotal(orderProducts));

        orderProducts.forEach(op -> op.setOrder(order));

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    @Transactional
    public OrderResponse updateOrder(Long id, UpdateOrderRequest request, Long storeId) {
        Order order = orderRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new SecurityException("주문을 수정할 수 없거나 접근 권한이 없습니다."));

        order.getOrderProducts().clear();

        List<OrderProduct> updatedProducts = request.orderProducts().stream()
                .map(productRequest -> {
                    Product product = productRepository.findById(productRequest.productId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID: " + productRequest.productId()));
                    return new OrderProduct(null, order, product, productRequest.quantity());
                }).collect(Collectors.toList());

        order.setOrderProducts(updatedProducts);

        order.setTotalPrice(calculateTotal(updatedProducts));
        order.setOrderDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    @Transactional
    public void deleteOrder(Long orderId, Long storeId) {
        Order order = orderRepository.findByIdAndStoreId(orderId, storeId)
                .orElseThrow(() -> new SecurityException("주문을 삭제할 수 없거나 접근 권한이 없습니다."));
        orderRepository.delete(order);
    }

    private Long calculateTotal(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToLong(op -> op.getProduct().getPrice() * op.getQuantity())
                .sum();
    }
}
