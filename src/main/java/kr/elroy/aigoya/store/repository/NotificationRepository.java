package kr.elroy.aigoya.store.repository;

import kr.elroy.aigoya.store.domain.Notification;
import kr.elroy.aigoya.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByStoreOrderByCreatedAtDesc(Store store);
}