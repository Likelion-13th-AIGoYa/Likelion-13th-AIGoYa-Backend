package kr.elroy.aigoya.ai.controller;

import kr.elroy.aigoya.ai.AiApi;
import kr.elroy.aigoya.ai.dto.request.ChatRequest;
import kr.elroy.aigoya.ai.dto.response.ChatMessageResponse;
import kr.elroy.aigoya.ai.dto.response.ReportResponse;
import kr.elroy.aigoya.ai.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AiController implements AiApi {
    private final AgentService agentService;

    public ReportResponse chat(Long storeId, ChatRequest request) {
        Map<String, Object> result = agentService.chat(storeId, request.chatRoomId(), request.message());

        Long chatRoomId = (Long) result.get("chatRoomId");
        String report = (String) result.get("report");

        return new ReportResponse(storeId, chatRoomId, report);
    }

    public List<ChatMessageResponse> getChatHistory(Long storeId, Long chatRoomId) {
        return agentService.getChatHistory(storeId, chatRoomId);
    }
}
