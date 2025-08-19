package kr.elroy.aigoya.order;

import kr.elroy.aigoya.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStoreId(Long storeId);
}