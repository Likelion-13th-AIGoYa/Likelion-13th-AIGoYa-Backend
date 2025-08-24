package kr.elroy.aigoya.ai.controller;

import kr.elroy.aigoya.ai.dto.request.ChatRequest;
import kr.elroy.aigoya.ai.dto.response.ReportResponse;
import kr.elroy.aigoya.ai.service.AgentService;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String message = request.message();

        String result = agentService.chat(storeId, message);

        return new ReportResponse(result);
    }
}
