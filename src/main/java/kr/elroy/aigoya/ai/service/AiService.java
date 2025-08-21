package kr.elroy.aigoya.ai.service;

import kr.elroy.aigoya.order.OrderRepository;
import kr.elroy.aigoya.order.domain.Order;
import kr.elroy.aigoya.order.domain.OrderProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AiService {

    private final ChatClient chatClient;
    private final PromptTemplate salesReportTemplate;
    private final PromptTemplate inventoryPredictionTemplate;
    private final PromptTemplate marketingCopyTemplate; // 1. 필드 추가
    private final OrderRepository orderRepository;

    public AiService(ChatClient.Builder chatClientBuilder,
                     @Value("classpath:/prompts/sales-report.st") Resource salesReportResource,
                     @Value("classpath:/prompts/inventory-prediction.st") Resource inventoryPredictionResource,
                     @Value("classpath:/prompts/marketing-copy.st") Resource marketingCopyResource, // 2. 프롬프트 로드
                     OrderRepository orderRepository) {
        this.chatClient = chatClientBuilder.build();
        this.salesReportTemplate = new PromptTemplate(salesReportResource);
        this.inventoryPredictionTemplate = new PromptTemplate(inventoryPredictionResource);
        this.marketingCopyTemplate = new PromptTemplate(marketingCopyResource); // 2. 프롬프트 초기화
        this.orderRepository = orderRepository;
    }

    public String generateSalesReport(Long storeId) {
        List<Order> orders = orderRepository.findAllByStoreId(storeId);
        String salesData = convertOrdersToSalesDataString(orders);
        return callAiModel(salesReportTemplate, Map.of("salesData", salesData), "보고서 생성");
    }

    public String predictInventory(Long storeId) {
        List<Order> orders = orderRepository.findAllByStoreId(storeId);
        String salesData = convertOrdersToSalesDataString(orders);
        return callAiModel(inventoryPredictionTemplate, Map.of("salesData", salesData), "재고 예측");
    }

    public String generateMarketingCopy(Long storeId, String userRequest) {
        List<Order> orders = orderRepository.findAllByStoreId(storeId);
        String salesData = convertOrdersToSalesDataString(orders);
        return callAiModel(marketingCopyTemplate, Map.of("salesData", salesData, "request", userRequest), "마케팅 문구 생성");
    }

    private String callAiModel(PromptTemplate promptTemplate, Map<String, Object> params, String taskName) {
        try {
            return chatClient.prompt(promptTemplate.create(params))
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("AI {} 중 오류 발생. params: {}", taskName, params, e);
            return "AI가 " + taskName + "을(를) 처리하는 중에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.";
        }
    }

    private String convertOrdersToSalesDataString(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "No sales data available.";
        }

        return orders.stream()
                .map(order -> {
                    String productsInfo = order.getOrderProducts().stream()
                            .map(op -> String.format("  - Product: %s, Quantity: %d, Price: %d",
                                    op.getProduct().getName(), op.getQuantity(), op.getOrderPrice()))
                            .collect(Collectors.joining("\n"));
                    return String.format("Order ID: %d, Store ID: %d, Ordered At: %s, Total Price: %d\n%s",
                            order.getId(), order.getStore().getId(), order.getOrderedAt(), order.getTotalPrice(), productsInfo);
                })
                .collect(Collectors.joining("\n\n"));
    }
}
