package kr.elroy.aigoya.ai.dto.response;

import kr.elroy.aigoya.ai.domain.ChatRoom;

public record ChatRoomResponse(
        Long chatRoomId,
        String title
) {
    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return new ChatRoomResponse(chatRoom.getId(), chatRoom.getTitle());
    }
}
