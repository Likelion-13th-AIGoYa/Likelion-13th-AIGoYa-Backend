package kr.elroy.aigoya.store.repository;

import kr.elroy.aigoya.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByEmail(String email);
    boolean existsByName(String name);

    Optional<Store> findByEmail(String email);
}