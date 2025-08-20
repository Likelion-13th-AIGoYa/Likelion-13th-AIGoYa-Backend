package kr.elroy.aigoya.product.service;

import kr.elroy.aigoya.product.repository.ProductRepository;
import kr.elroy.aigoya.product.domain.Product;
import kr.elroy.aigoya.product.dto.request.CreateProductRequest;
import kr.elroy.aigoya.product.dto.request.UpdateProductRequest;
import kr.elroy.aigoya.store.domain.Store;
import kr.elroy.aigoya.store.repository.StoreRepository;
import kr.elroy.aigoya.store.exception.StoreNotFoundException;
import kr.elroy.aigoya.product.exception.ProductNotFoundException;
import kr.elroy.aigoya.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public Product createProduct(Long storeId, CreateProductRequest request) {
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
        Product newProduct = Product.builder()
                .name(request.name())
                .price(request.price())
                .store(store)
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
        product.updateInfo(request.name(), request.price());
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