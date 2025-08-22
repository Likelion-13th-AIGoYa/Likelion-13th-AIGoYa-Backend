package kr.elroy.aigoya.ai.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByStoreIdOrderByCreatedAtDesc(Long storeId, Pageable pageable);
}
