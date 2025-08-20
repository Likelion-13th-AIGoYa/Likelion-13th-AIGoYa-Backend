package kr.elroy.aigoya.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AiService {

    private final ChatClient chatClient;
    private final PromptTemplate salesReportTemplate;
    private final PromptTemplate inventoryPredictionTemplate;

    public AiService(ChatClient.Builder chatClientBuilder,
                     @Value("classpath:/prompts/sales-report.st") Resource salesReportResource,
                     @Value("classpath:/prompts/inventory-prediction.st") Resource inventoryPredictionResource) {
        this.chatClient = chatClientBuilder.build();
        this.salesReportTemplate = new PromptTemplate(salesReportResource);
        this.inventoryPredictionTemplate = new PromptTemplate(inventoryPredictionResource);
    }

    public String generateSalesReport(String salesData) {
        return callAiModel(salesReportTemplate, Map.of("salesData", salesData), "보고서 생성");
    }

    public String predictInventory(String salesData) {
        return callAiModel(inventoryPredictionTemplate, Map.of("salesData", salesData), "재고 예측");
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
}
