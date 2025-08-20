package kr.elroy.aigoya.store.service;

import kr.elroy.aigoya.exception.AccessDeniedException;
import kr.elroy.aigoya.store.domain.Notification;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.exception.NotificationNotFoundException;
import kr.elroy.aigoya.store.exception.StoreNotFoundException;
import kr.elroy.aigoya.store.repository.NotificationRepository;
import kr.elroy.aigoya.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

        return notificationRepository.findByStoreOrderByCreatedAtDesc(store);
    }

    public void markAsRead(Long storeId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);

        if (!notification.getStore().getId().equals(storeId)) {
            throw new AccessDeniedException();
        }

        notification.read();
    }

    public Notification createNotification(Long storeId, String message) {
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

        Notification notification = Notification.builder()
                .store(store)
                .message(message)
                .build();

        return notificationRepository.save(notification);
    }
}