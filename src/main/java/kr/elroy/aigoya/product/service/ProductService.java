package kr.elroy.aigoya.product.service;

import kr.elroy.aigoya.category.domain.Category;
import kr.elroy.aigoya.category.exception.CategoryNotFoundException;
import kr.elroy.aigoya.category.repository.CategoryRepository;
import kr.elroy.aigoya.exception.AccessDeniedException;
import kr.elroy.aigoya.product.domain.Product;
import kr.elroy.aigoya.product.dto.request.CreateProductRequest;
import kr.elroy.aigoya.product.dto.request.UpdateProductRequest;
import kr.elroy.aigoya.product.exception.ProductNotFoundException;
import kr.elroy.aigoya.product.repository.ProductRepository;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.exception.StoreNotFoundException;
import kr.elroy.aigoya.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    public Product createProduct(Long storeId, CreateProductRequest request) {
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(CategoryNotFoundException::new);
        if (!Objects.equals(category.getStore().getId(), storeId)) {
            throw new CategoryNotFoundException();
        }

        Product newProduct = Product.builder()
                .name(request.name())
                .price(request.price())
                .store(store)
                .category(category)
                .build();
        return productRepository.save(newProduct);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByStore(Long storeId) {
        return productRepository.findByStoreId(storeId);
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long storeId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        if (!product.getStore().getId().equals(storeId)) {
            throw new AccessDeniedException();
        }

        return product;
    }

    public Product updateProduct(Long storeId, Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        if (!product.getStore().getId().equals(storeId)) {
            throw new AccessDeniedException();
        }

        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(CategoryNotFoundException::new);
            if (!Objects.equals(category.getStore().getId(), storeId)) {
                throw new CategoryNotFoundException();
            }
        }

        product.updateInfo(request.name(), request.price(), category);
        return product;
    }

    public void deleteProduct(Long storeId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        if (!product.getStore().getId().equals(storeId)) {
            throw new AccessDeniedException();
        }

        productRepository.delete(product);
    }
}
