package kr.elroy.aigoya.ai.dto.response;

import kr.elroy.aigoya.ai.domain.ChatMessage;
import kr.elroy.aigoya.ai.domain.ChatRole;

import java.time.LocalDateTime;

public record ChatMessageResponse(
    Long id,
    ChatRole role,
    String content,
    LocalDateTime createdAt
) {
    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return new ChatMessageResponse(
            chatMessage.getId(),
            chatMessage.getRole(),
            chatMessage.getContent(),
            chatMessage.getCreatedAt()
        );
    }
}
