package kr.elroy.aigoya.category.repository;

import kr.elroy.aigoya.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByStoreId(Long storeId);

    boolean existsByStoreIdAndName(Long storeId, String name);
}
