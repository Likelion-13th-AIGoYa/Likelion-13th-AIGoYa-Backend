package kr.elroy.aigoya.ai.controller;

import kr.elroy.aigoya.ai.dto.request.ReportRequest;
import kr.elroy.aigoya.ai.dto.response.ReportResponse;
import kr.elroy.aigoya.ai.service.AiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/report")
    public ReportResponse generateReport(@RequestBody ReportRequest request) {
        String result = aiService.generateSalesReport(request.salesData());
        return new ReportResponse(result);
    }

    @PostMapping("/inventory")
    public ReportResponse predictInventory(@RequestBody ReportRequest request) {
        String result = aiService.predictInventory(request.salesData());
        return new ReportResponse(result);
    }
}
