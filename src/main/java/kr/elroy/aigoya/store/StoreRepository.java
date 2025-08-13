package kr.elroy.aigoya.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Store findByEmail(String email);

    Store findByName(String name);
}
