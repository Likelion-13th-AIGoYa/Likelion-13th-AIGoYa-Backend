package kr.elroy.aigoya.store.controller;

import kr.elroy.aigoya.store.api.NotificationApi;
import kr.elroy.aigoya.store.domain.Notification;
import kr.elroy.aigoya.store.dto.response.NotificationResponse;
import kr.elroy.aigoya.store.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;

    @Override
    public List<NotificationResponse> getNotifications(Long storeId) {
        List<Notification> notifications = notificationService.getNotificationsForStore(storeId);
        return notifications.stream()
                .map(NotificationResponse::of)
                .toList();
    }

    @Override
    public void markAsRead(
            Long storeId,
            Long notificationId
    ) {
        notificationService.markAsRead(storeId, notificationId);
    }
}