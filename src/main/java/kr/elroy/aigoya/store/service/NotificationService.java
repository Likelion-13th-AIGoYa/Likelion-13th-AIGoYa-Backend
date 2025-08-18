package kr.elroy.aigoya.store.service;

import kr.elroy.aigoya.store.domain.Notification;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final StoreService storeService;

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForStore(Long storeId) {
        Store store = storeService.getStore(storeId);
        return notificationRepository.findByStoreOrderByCreatedAtDesc(store);
    }

    public void markAsRead(Long storeId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));

        if (!notification.getStore().getId().equals(storeId)) {
            throw new IllegalArgumentException("해당 가게의 알림이 아닙니다.");
        }

        notification.read();
    }

    public Notification createNotification(Long storeId, String message) {
        Store store = storeService.getStore(storeId);

        Notification notification = Notification.builder()
                .store(store)
                .message(message)
                .build();

        return notificationRepository.save(notification);
    }
}