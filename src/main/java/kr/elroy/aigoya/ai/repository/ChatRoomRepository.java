package kr.elroy.aigoya.ai.repository;

import kr.elroy.aigoya.ai.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
