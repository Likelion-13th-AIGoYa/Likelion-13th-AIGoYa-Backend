package kr.elroy.aigoya.ai.controller;

import kr.elroy.aigoya.ai.dto.request.ChatRequest;
import kr.elroy.aigoya.ai.dto.response.ChatMessageResponse;
import kr.elroy.aigoya.ai.dto.response.ReportResponse;
import kr.elroy.aigoya.ai.service.AgentService;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AgentService agentService;

    @PostMapping("/chat")
    public ReportResponse chat(
            @CurrentStoreId Long storeId,
            @RequestBody ChatRequest request
    ) {
        Map<String, Object> result = agentService.chat(storeId, request.chatRoomId(), request.message());

        Long chatRoomId = (Long) result.get("chatRoomId");
        String report = (String) result.get("report");

        return new ReportResponse(storeId, chatRoomId, report);
    }

    @GetMapping("/chat/history/{chatRoomId}")
    public List<ChatMessageResponse> getChatHistory(
            @CurrentStoreId Long storeId,
            @PathVariable Long chatRoomId
    ) {
        return agentService.getChatHistory(storeId, chatRoomId);
    }
}
