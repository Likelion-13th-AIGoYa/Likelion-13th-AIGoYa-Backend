package kr.elroy.aigoya.order;

import kr.elroy.aigoya.exception.AccessDeniedException;
import kr.elroy.aigoya.order.domain.Order;
import kr.elroy.aigoya.order.domain.OrderProduct;
import kr.elroy.aigoya.order.dto.request.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.request.OrderProductDto;
import kr.elroy.aigoya.order.dto.request.UpdateOrderRequest;
import kr.elroy.aigoya.order.dto.response.OrderResponse;
import kr.elroy.aigoya.order.exception.OrderNotFoundException;
import kr.elroy.aigoya.product.Product;
import kr.elroy.aigoya.product.ProductRepository;
import kr.elroy.aigoya.product.exception.ProductNotFoundException;
import kr.elroy.aigoya.store.Store;
import kr.elroy.aigoya.store.StoreRepository;
import kr.elroy.aigoya.store.exception.StoreNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long currentStoreId, Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        validateOrderOwnership(currentStoreId, order);

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(Long currentStoreId) {
        return orderRepository
                .findAllByStoreId(currentStoreId)
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional
    public OrderResponse createOrder(Long currentStoreId, CreateOrderRequest request) {
        Store store = storeRepository.findById(currentStoreId).orElseThrow(StoreNotFoundException::new);
        Order order = Order.builder()
                .orderedAt(request.orderedAt())
                .store(store)
                .build();

        updateOrderProducts(currentStoreId, order, request.orderProducts());
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    @Transactional
    public OrderResponse updateOrder(Long currentStoreId, Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        validateOrderOwnership(currentStoreId, order);

        updateOrderProducts(currentStoreId, order, request.orderProducts());
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    @Transactional
    public void deleteOrder(Long currentStoreId, Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        validateOrderOwnership(currentStoreId, order);

        orderRepository.delete(order);
    }

    private OrderProduct createOrderProduct(Long currentStoreId, Order order, OrderProductDto orderProductDto) {
        Product product = productRepository
                .findById(orderProductDto.productId())
                .orElseThrow(ProductNotFoundException::new);

        validateProductOwnership(currentStoreId, product);

        return OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(orderProductDto.quantity())
                .build();
    }

    private Long calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToLong(orderProduct ->
                        orderProduct.getProduct().getPrice() * orderProduct.getQuantity()
                )
                .sum();
    }

    private void updateOrderProducts(Long currentStoreId, Order order, List<OrderProductDto> newOrderProductDtos) {
        List<OrderProduct> newOrderProducts = newOrderProductDtos
                .stream()
                .map(orderProductDto -> createOrderProduct(currentStoreId, order, orderProductDto))
                .toList();

        order.setTotalPrice(calculateTotalPrice(newOrderProducts));
        order.setOrderProducts(newOrderProducts);
    }

    private void validateOrderOwnership(Long currentStoreId, Order order) {
        if (!order.getStore().getId().equals(currentStoreId)) {
            throw new AccessDeniedException();
        }
    }

    private void validateProductOwnership(Long currentStoreId, Product product) {
        if (!product.getStore().getId().equals(currentStoreId)) {
            throw new AccessDeniedException();
        }
    }
}