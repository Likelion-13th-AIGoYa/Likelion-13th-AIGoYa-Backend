package kr.elroy.aigoya.product.controller;

import kr.elroy.aigoya.product.api.ProductApi;
import kr.elroy.aigoya.product.domain.Product;
import kr.elroy.aigoya.product.dto.request.CreateProductRequest;
import kr.elroy.aigoya.product.dto.request.UpdateProductRequest;
import kr.elroy.aigoya.product.dto.response.ProductResponse;
import kr.elroy.aigoya.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ProductResponse createProduct(Long storeId, CreateProductRequest request) {
        Product createdProduct = productService.createProduct(storeId, request);
        return ProductResponse.from(createdProduct);
    }

    @Override
    public ProductResponse getProduct(Long storeId, Long productId) {
        Product product = productService.getProduct(storeId, productId);
        return ProductResponse.from(product);
    }

    @Override
    public List<ProductResponse> getAllProducts(Long storeId) {
        return productService.getProductsByStore(storeId).stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(Long storeId, Long productId, UpdateProductRequest request) {
        Product updatedProduct = productService.updateProduct(storeId, productId, request);
        return ProductResponse.from(updatedProduct);
    }
    @Override
    public void deleteProduct(Long storeId, Long productId) {
        productService.deleteProduct(storeId, productId);
    }
}