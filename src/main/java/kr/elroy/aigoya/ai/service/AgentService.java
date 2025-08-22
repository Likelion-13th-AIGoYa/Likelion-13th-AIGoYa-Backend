package kr.elroy.aigoya.ai.service;

import kr.elroy.aigoya.ai.domain.ChatMessage;
import kr.elroy.aigoya.ai.domain.ChatMessageRepository;
import kr.elroy.aigoya.ai.domain.ChatRole;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AgentService {

    private static final int HISTORY_SIZE = 5;

    private final AiService aiService;
    private final StoreRepository storeRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatClient chatClient;
    private final PromptTemplate routerPromptTemplate;
    private final PromptTemplate finalSynthesisPromptTemplate;
    private final PromptTemplate summaryPromptTemplate;

    public AgentService(AiService aiService, StoreRepository storeRepository, ChatMessageRepository chatMessageRepository, ChatClient.Builder chatClientBuilder,
                        @Value("classpath:/prompts/ai-router-prompt.st") Resource routerResource,
                        @Value("classpath:/prompts/final-synthesis-prompt.st") Resource finalSynthesisResource,
                        @Value("classpath:/prompts/history-summary-prompt.st") Resource summaryResource) {
        this.aiService = aiService;
        this.storeRepository = storeRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatClient = chatClientBuilder.build();
        this.routerPromptTemplate = new PromptTemplate(routerResource);
        this.finalSynthesisPromptTemplate = new PromptTemplate(finalSynthesisResource);
        this.summaryPromptTemplate = new PromptTemplate(summaryResource);
    }

    public String chat(Long storeId, String userMessage) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + storeId));

        saveMessage(store, ChatRole.USER, userMessage);

        String fullChatHistory = getFormattedChatHistory(storeId);

        String historySummary = summarizeChatHistory(fullChatHistory);
        log.info("Chat history summarized: {}", historySummary);

        String routerInput = historySummary + "\nUSER: " + userMessage;
        String[] toolNames = getRouteFromAi(routerInput).split(",");
        log.info("AI Router selected tool(s): {}", (Object) toolNames);

        String finalResponse;

        if (toolNames.length > 0 && !toolNames[0].equals("CHAT")) {
            Map<String, String> toolResults = new HashMap<>();
            for (String toolName : toolNames) {
                String result = executeTool(toolName.trim(), storeId, userMessage, historySummary);
                toolResults.put(toolName.trim(), result);
            }

            finalResponse = synthesizeFinalResponse(historySummary, toolResults);

        } else {
            finalResponse = "죄송합니다, 요청을 이해하지 못했습니다. '매출 보고서', '재고 예측', '마케팅 문구' 등으로 말씀해주세요.";
        }

        saveMessage(store, ChatRole.AI, finalResponse);
        return finalResponse;
    }

    private String executeTool(String toolName, Long storeId, String userMessage, String chatHistorySummary) {
        switch (toolName) {
            case "generateSalesReport":
                return aiService.generateSalesReport(storeId, chatHistorySummary);
            case "predictInventory":
                return aiService.predictInventory(storeId, chatHistorySummary);
            case "generateMarketingCopy":
                return aiService.generateMarketingCopy(storeId, userMessage, chatHistorySummary);
            case "analyzeSalesByWeather":
                return aiService.analyzeSalesByWeather(storeId, chatHistorySummary);
            case "getWeather":
                return aiService.getCurrentWeather(storeId);
            default:
                return "알 수 없는 도구입니다: " + toolName;
        }
    }

    private String synthesizeFinalResponse(String chatHistorySummary, Map<String, String> toolResults) {
        String formattedResults = toolResults.entrySet().stream()
                .map(entry -> "--- " + entry.getKey() + " 결과 ---\n" + entry.getValue())
                .collect(Collectors.joining("\n\n"));

        return chatClient.prompt(finalSynthesisPromptTemplate.create(Map.of(
                        "chatHistory", chatHistorySummary,
                        "toolResults", formattedResults
                )))
                .call()
                .content();
    }

    private String getRouteFromAi(String routerInput) {
        return chatClient.prompt(routerPromptTemplate.create(Map.of("chatHistory", routerInput)))
                .call()
                .content();
    }

    private String summarizeChatHistory(String fullChatHistory) {
        if (fullChatHistory.isBlank()) {
            return "No previous conversation.";
        }
        return chatClient.prompt(summaryPromptTemplate.create(Map.of("chatHistory", fullChatHistory)))
                .call()
                .content();
    }

    private String getFormattedChatHistory(Long storeId) {
        Pageable pageable = PageRequest.of(0, HISTORY_SIZE);
        List<ChatMessage> recentMessages = chatMessageRepository.findByStoreIdOrderByCreatedAtDesc(storeId, pageable);
        Collections.reverse(recentMessages);
        return recentMessages.stream()
                .map(message -> message.getRole().name() + ": " + message.getContent())
                .collect(Collectors.joining("\n"));
    }

    private void saveMessage(Store store, ChatRole role, String content) {
        ChatMessage message = ChatMessage.builder()
                .store(store)
                .role(role)
                .content(content)
                .build();
        chatMessageRepository.save(message);
    }
}
