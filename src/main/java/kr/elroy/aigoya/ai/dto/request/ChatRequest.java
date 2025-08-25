package kr.elroy.aigoya.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        Long chatRoomId,
        @NotBlank String message
) {
}
