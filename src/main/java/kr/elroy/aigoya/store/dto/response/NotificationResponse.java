package kr.elroy.aigoya.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.elroy.aigoya.store.domain.Notification;

import java.time.LocalDateTime;

@Schema(description = "알림 정보 응답")
public record NotificationResponse(
        @Schema(description = "알림 ID", example = "201")
        Long id,

        @Schema(description = "알림 메시지 내용", example = "새로운 주문이 접수되었습니다.")
        String message,

        @Schema(description = "읽음 여부", example = "false")
        boolean read,

        @Schema(description = "알림 생성 시각", example = "2025-08-17T17:15:00")
        LocalDateTime createdAt
) {
    public static NotificationResponse of(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}