package kr.elroy.aigoya.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AiChatRequest(@NotBlank String message) {}
