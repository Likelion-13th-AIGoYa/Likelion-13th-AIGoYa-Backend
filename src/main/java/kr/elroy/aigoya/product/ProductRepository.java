package kr.elroy.aigoya.product;

import kr.elroy.aigoya.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
