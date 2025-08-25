package kr.elroy.aigoya.ai;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.ai.dto.request.ChatRequest;
import kr.elroy.aigoya.ai.dto.response.ChatMessageResponse;
import kr.elroy.aigoya.ai.dto.response.ReportResponse;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "AI 채팅", description = "AI 채팅 API (인증 필요)")
@Validated
@RequestMapping("/api/ai")
public interface AiApi {

    @Operation(summary = "AI 채팅", description = "AI와 채팅하고 보고서를 생성합니다.")
    @PostMapping("/chat")
    ReportResponse chat(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId,

            @Valid
            @RequestBody
            ChatRequest request
    );

    @Operation(summary = "채팅 히스토리 조회", description = "특정 채팅방의 대화 히스토리를 조회합니다.")
    @GetMapping("/chat/history/{chatRoomId}")
    List<ChatMessageResponse> getChatHistory(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId,

            @Parameter(description = "채팅방 ID", required = true, example = "1")
            @PathVariable
            Long chatRoomId
    );
}
