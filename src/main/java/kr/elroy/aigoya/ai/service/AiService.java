package kr.elroy.aigoya.ai.service;

import kr.elroy.aigoya.order.OrderRepository;
import kr.elroy.aigoya.order.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AiService {

    private final ChatClient chatClient;
    private final PromptTemplate salesReportTemplate;
    private final PromptTemplate inventoryPredictionTemplate;
    private final PromptTemplate marketingCopyTemplate;
    private final PromptTemplate salesAnalysisByWeatherTemplate;
    private final OrderRepository orderRepository;
    private final WeatherService weatherService;

    public AiService(ChatClient.Builder chatClientBuilder,
                     @Value("classpath:/prompts/sales-report.st") Resource salesReportResource,
                     @Value("classpath:/prompts/inventory-prediction.st") Resource inventoryPredictionResource,
                     @Value("classpath:/prompts/marketing-copy.st") Resource marketingCopyResource,
                     @Value("classpath:/prompts/sales-analysis-by-weather-prompt.st") Resource salesAnalysisByWeatherResource,
                     OrderRepository orderRepository,
                     WeatherService weatherService) {
        this.chatClient = chatClientBuilder.build();
        this.salesReportTemplate = new PromptTemplate(salesReportResource);
        this.inventoryPredictionTemplate = new PromptTemplate(inventoryPredictionResource);
        this.marketingCopyTemplate = new PromptTemplate(marketingCopyResource);
        this.salesAnalysisByWeatherTemplate = new PromptTemplate(salesAnalysisByWeatherResource);
        this.orderRepository = orderRepository;
        this.weatherService = weatherService;
    }

    @Transactional(readOnly = true)
    public String getCurrentWeather(Long storeId) {
        return weatherService.getWeatherForStore(storeId, LocalDate.now());
    }

    @Transactional(readOnly = true)
    public String generateSalesReport(Long storeId, String chatHistory) {
        List<Order> orders = orderRepository.findAllByStoreId(storeId);
        String salesData = convertOrdersToSalesDataString(orders);
        return callAiModel(salesReportTemplate, Map.of("salesData", salesData, "chatHistory", chatHistory), "보고서 생성");
    }

    @Transactional(readOnly = true)
    public String predictInventory(Long storeId, String chatHistory) {
        List<Order> orders = orderRepository.findAllByStoreId(storeId);
        String salesData = convertOrdersToSalesDataString(orders);
        return callAiModel(inventoryPredictionTemplate, Map.of("salesData", salesData, "chatHistory", chatHistory), "재고 예측");
    }

    @Transactional(readOnly = true)
    public String generateMarketingCopy(Long storeId, String userRequest, String chatHistory) {
        List<Order> orders = orderRepository.findAllByStoreId(storeId);
        String salesData = convertOrdersToSalesDataString(orders);
        return callAiModel(marketingCopyTemplate, Map.of("salesData", salesData, "request", userRequest, "chatHistory", chatHistory), "마케팅 문구 생성");
    }

    @Transactional(readOnly = true)
    public String analyzeSalesByWeather(Long storeId, String chatHistory) {
        List<Order> orders = orderRepository.findAllByStoreId(storeId);

        String salesAndWeatherData = orders.stream()
                .flatMap(order -> {
                    String weather = weatherService.getWeatherForStore(storeId, order.getOrderedAt().toLocalDate());
                    return order.getOrderProducts().stream()
                            .map(op -> String.format("날짜: %s, 날씨: %s, 상품: %s, 수량: %d",
                                    order.getOrderedAt().toLocalDate(), weather, op.getProduct().getName(), op.getQuantity()));
                })
                .collect(Collectors.joining("\n"));

        log.info("Sales and Weather Data for AI Analysis:\n{}", salesAndWeatherData);

        return callAiModel(salesAnalysisByWeatherTemplate, Map.of("salesAndWeatherData", salesAndWeatherData, "chatHistory", chatHistory), "날씨 기반 분석");
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
                                    op.getProduct().getName(), op.getQuantity(), op.getProduct().getPrice()))
                            .collect(Collectors.joining("\n"));
                    return String.format("Order ID: %d, Store ID: %d, Ordered At: %s, Total Price: %d\n%s",
                            order.getId(), order.getStore().getId(), order.getOrderedAt(), order.getTotalPrice(), productsInfo);
                })
                .collect(Collectors.joining("\n\n"));
    }
}
