package kr.elroy.aigoya.ai.controller;

import kr.elroy.aigoya.ai.dto.request.AiChatRequest;
import kr.elroy.aigoya.ai.dto.response.ReportResponse;
import kr.elroy.aigoya.ai.service.AiService;
import kr.elroy.aigoya.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat")
    public ReportResponse chat(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AiChatRequest request
    ) {
        Long storeId = userDetails.getStore().getId();
        String message = request.message();
        String result;

        if (message.contains("매출") || message.contains("보고서")) {
            result = aiService.generateSalesReport(storeId);
        } else if (message.contains("재고") || message.contains("예측")) {
            result = aiService.predictInventory(storeId);
        } else if (message.contains("마케팅") || message.contains("문구") || message.contains("메시지")) {
            result = aiService.generateMarketingCopy(storeId, message);
        } else {
            result = "죄송합니다, 요청을 이해하지 못했습니다. '매출 보고서', '재고 예측', '마케팅 문구' 등으로 말씀해주세요.";
        }

        return new ReportResponse(result);
    }
}
