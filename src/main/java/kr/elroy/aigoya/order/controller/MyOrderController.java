package kr.elroy.aigoya.order.controller;

import kr.elroy.aigoya.order.OrderService;
import kr.elroy.aigoya.order.api.MyOrderApi;
import kr.elroy.aigoya.order.dto.request.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.request.UpdateOrderRequest;
import kr.elroy.aigoya.order.dto.response.OrderResponse;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyOrderController implements MyOrderApi {

    private final OrderService orderService;

    @Override
    public List<OrderResponse> getMyOrders(@CurrentStoreId Long storeId) {
        return orderService.getOrdersByStore(storeId);
    }

    @Override
    public OrderResponse getMyOrder(Long id, @CurrentStoreId Long storeId) {
        return orderService.getOrder(id, storeId);
    }

    @Override
    public OrderResponse createMyOrder(CreateOrderRequest request, @CurrentStoreId Long storeId) {
        return orderService.createOrder(request, storeId);
    }

    @Override
    public OrderResponse updateMyOrder(Long id, UpdateOrderRequest request, @CurrentStoreId Long storeId) {
        return orderService.updateOrder(id, request, storeId);
    }

    @Override
    public void deleteMyOrder(Long id, @CurrentStoreId Long storeId) {
        orderService.deleteOrder(id, storeId);
    }
}
