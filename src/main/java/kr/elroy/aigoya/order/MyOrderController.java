package kr.elroy.aigoya.order;

import kr.elroy.aigoya.order.api.MyOrderApi;
import kr.elroy.aigoya.order.dto.request.CreateOrderRequest;
import kr.elroy.aigoya.order.dto.request.UpdateOrderRequest;
import kr.elroy.aigoya.order.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyOrderController implements MyOrderApi {
    private final MyOrderService myOrderService;

    @Override
    public List<OrderResponse> getMyOrders(Long storeId) {
        return myOrderService.getAllOrders(storeId);
    }

    @Override
    public OrderResponse getMyOrder(Long id, Long storeId) {
        return myOrderService.getOrder(storeId, id);
    }

    @Override
    public OrderResponse createMyOrder(CreateOrderRequest request, Long storeId) {
        return myOrderService.createOrder(storeId, request);
    }

    @Override
    public OrderResponse updateMyOrder(Long id, UpdateOrderRequest request, Long storeId) {
        return myOrderService.updateOrder(storeId, id, request);
    }

    @Override
    public void deleteMyOrder(Long id, Long storeId) {
        myOrderService.deleteOrder(storeId, id);
    }
}
