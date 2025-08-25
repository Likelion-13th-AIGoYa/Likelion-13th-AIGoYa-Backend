package kr.elroy.aigoya.ai.service;

import kr.elroy.aigoya.ai.domain.ChatMessage;
import kr.elroy.aigoya.ai.repository.ChatMessageRepository;
import kr.elroy.aigoya.ai.domain.ChatRole;
import kr.elroy.aigoya.ai.domain.ChatRoom;
import kr.elroy.aigoya.ai.repository.ChatRoomRepository;
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
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.elroy.aigoya.ai.dto.response.ChatMessageResponse;

@Slf4j
@Service
@Transactional
public class AgentService {

    private static final int HISTORY_SIZE = 10;

    private final AiService aiService;
    private final StoreRepository storeRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatClient chatClient;
    private final PromptTemplate routerPromptTemplate;
    private final PromptTemplate finalSynthesisPromptTemplate;
    private final PromptTemplate summaryPromptTemplate;
    private final PromptTemplate generalConversationPromptTemplate;
    private final PromptTemplate topicContinuityPromptTemplate;

    public AgentService(AiService aiService, StoreRepository storeRepository, ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository, ChatClient.Builder chatClientBuilder,
                        @Value("classpath:/prompts/ai-router-prompt.st") Resource routerResource,
                        @Value("classpath:/prompts/final-synthesis-prompt.st") Resource finalSynthesisResource,
                        @Value("classpath:/prompts/history-summary-prompt.st") Resource summaryResource,
                        @Value("classpath:/prompts/general-conversation-prompt.st") Resource generalConversationResource,
                        @Value("classpath:/prompts/topic-continuity-prompt.st") Resource topicContinuityResource) {
        this.aiService = aiService;
        this.storeRepository = storeRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatClient = chatClientBuilder.build();
        this.routerPromptTemplate = new PromptTemplate(routerResource);
        this.finalSynthesisPromptTemplate = new PromptTemplate(finalSynthesisResource);
        this.summaryPromptTemplate = new PromptTemplate(summaryResource);
        this.generalConversationPromptTemplate = new PromptTemplate(generalConversationResource);
        this.topicContinuityPromptTemplate = new PromptTemplate(topicContinuityResource);
    }

    public Map<String, Object> chat(Long storeId, Long chatRoomId, String userMessage) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + storeId));

        ChatRoom chatRoom;
        if (chatRoomId == null) {
            chatRoom = ChatRoom.from(store);
            chatRoomRepository.save(chatRoom);
        } else {
            chatRoom = chatRoomRepository.findById(chatRoomId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid chatRoom Id:" + chatRoomId));
            // 채팅방이 해당 storeId에 속하는지 검증
            if (!chatRoom.getStore().getId().equals(storeId)) {
                throw new IllegalArgumentException("ChatRoom does not belong to the provided storeId.");
            }
        }

        String fullChatHistory = getFormattedChatHistory(chatRoom.getId());
        boolean isContinuation = isTopicContinuation(fullChatHistory, userMessage);
        log.info("Is topic continuation? {}", isContinuation);

        saveMessage(store, chatRoom, ChatRole.USER, userMessage);

        String historySummary;
        if (isContinuation) {
            historySummary = summarizeChatHistory(fullChatHistory);
            log.info("Chat history summarized: {}", historySummary);
        } else {
            historySummary = "No relevant previous conversation.";
            log.info("Starting a new topic. No history summary will be used.");
        }

        String routerInput = historySummary + "\nUSER: " + userMessage;
        String route = getRouteFromAi(routerInput);
        log.info("AI Router selected tool: {}", route);

        String finalResponse;

        if ("CHAT".equals(route)) {
            finalResponse = chatClient.prompt(generalConversationPromptTemplate.create(Map.of("chatHistory", routerInput)))
                    .call()
                    .content();
        } else if (route.isBlank()) {
            finalResponse = "죄송합니다, 요청을 이해하지 못했습니다. '매출 보고서', '재고 예측', '마케팅 문구' 등으로 말씀해주세요.";
        } else {
            String[] toolNames = route.split(",");
            Map<String, String> toolResults = new HashMap<>();
            for (String toolName : toolNames) {
                String result = executeTool(toolName.trim(), storeId, userMessage, historySummary);
                toolResults.put(toolName.trim(), result);
            }
            finalResponse = synthesizeFinalResponse(historySummary, toolResults);
        }

        saveMessage(store, chatRoom, ChatRole.AI, finalResponse);

        Map<String, Object> result = new HashMap<>();
        result.put("chatRoomId", chatRoom.getId());
        result.put("report", finalResponse);
        return result;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatHistory(Long storeId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chatRoom Id:" + chatRoomId));

        // 채팅방이 해당 storeId에 속하는지 검증
        if (!chatRoom.getStore().getId().equals(storeId)) {
            throw new IllegalArgumentException("ChatRoom does not belong to the provided storeId.");
        }

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
        return messages.stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }

    private boolean isTopicContinuation(String chatHistory, String userMessage) {
        if (!StringUtils.hasText(chatHistory)) {
            return false;
        }

        Map<String, Object> model = Map.of(
                "chatHistory", chatHistory,
                "userMessage", userMessage
        );

        String decision = chatClient.prompt(topicContinuityPromptTemplate.create(model))
                .call()
                .content();

        return "CONTINUE".equalsIgnoreCase(decision.trim());
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

    private String getFormattedChatHistory(Long chatRoomId) {
        Pageable pageable = PageRequest.of(0, HISTORY_SIZE);
        List<ChatMessage> recentMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable);
        Collections.reverse(recentMessages);
        return recentMessages.stream()
                .map(message -> message.getRole().name() + ": " + message.getContent())
                .collect(Collectors.joining("\n"));
    }

    private void saveMessage(Store store, ChatRoom chatRoom, ChatRole role, String content) {
        ChatMessage message = ChatMessage.builder()
                .store(store)
                .chatRoom(chatRoom)
                .role(role)
                .content(content)
                .build();
        chatMessageRepository.save(message);
    }
}
