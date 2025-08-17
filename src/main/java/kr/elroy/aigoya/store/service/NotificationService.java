package kr.elroy.aigoya.store.service;

import kr.elroy.aigoya.store.domain.Notification;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.repository.NotificationRepository;
import kr.elroy.aigoya.store.service.StoreService; // StoreRepository 대신 주입
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

    /**
     * 특정 가게의 모든 알림을 조회(최신순)
     */
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForStore(Long storeId) {
        Store store = storeService.getStore(storeId);
        return notificationRepository.findByStoreOrderByCreatedAtDesc(store);
    }

    /**
     * 알림을 읽음 처리
     * @param storeId 현재 로그인한 가게 ID (권한 확인용)
     * @param notificationId 읽음 처리할 알림 ID
     */
    public void markAsRead(Long storeId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));

        if (!notification.getStore().getId().equals(storeId)) {
            throw new IllegalArgumentException("해당 가게의 알림이 아닙니다."); // 혹은 접근 권한 없음 예외 처리
        }

        notification.read();
    }

    /**
     * (내부용) 시스템이 가게에 알림을 생성할 때 호출하는 메서드
     */
    public Notification createNotification(Long storeId, String message) {
        Store store = storeService.getStore(storeId);

        Notification notification = Notification.builder()
                .store(store)
                .message(message)
                .build();

        return notificationRepository.save(notification);
    }
}