package kr.elroy.aigoya.ai.dto.request;

import kr.elroy.aigoya.order.domain.Order;
import kr.elroy.aigoya.store.domain.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AiAnalysisRequest {

    private Long storeId;
    private String storeName;
    private List<OrderInfo> orders;

    public static AiAnalysisRequest from(Store store, List<Order> orders) {
        List<OrderInfo> orderInfos = orders.stream()
                .map(OrderInfo::from)
                .collect(Collectors.toList());
        return new AiAnalysisRequest(store.getId(), store.getName(), orderInfos);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderInfo {
        private Long orderId;
        private long totalPrice; // 주문 총액

        public static OrderInfo from(Order order) {
            // 실제 Order Entity의 총액을 가져오는 로직으로 수정해야 합니다.
            // 예: return new OrderInfo(order.getId(), order.calculateTotalPrice());
            return new OrderInfo(order.getId(), 15000L); // 임시 값
        }
    }
}