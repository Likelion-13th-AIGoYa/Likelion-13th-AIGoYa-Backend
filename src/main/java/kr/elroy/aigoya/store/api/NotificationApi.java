package kr.elroy.aigoya.store.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import kr.elroy.aigoya.store.dto.response.NotificationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "알림", description = "내 가게의 알림 관리 API (인증 필요)")
@RequestMapping("/v1/stores/me/notifications")
public interface NotificationApi {

    @GetMapping
    List<NotificationResponse> getNotifications(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId
    );

    @PostMapping("/{notificationId}/read")
    void markAsRead(
            @Parameter(hidden = true)
            @CurrentStoreId
            Long storeId,

            @PathVariable
            Long notificationId
    );
}