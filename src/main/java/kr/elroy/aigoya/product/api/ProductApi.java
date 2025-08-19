package kr.elroy.aigoya.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.product.dto.request.CreateProductRequest;
import kr.elroy.aigoya.product.dto.request.UpdateProductRequest;
import kr.elroy.aigoya.product.dto.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품 관리", description = "내 가게의 상품 관리 API (인증 필요)")
@RequestMapping("/v1/stores/me/products")
public interface ProductApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "새 상품 등록")
    ProductResponse createProduct(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "id") Long storeId,
            @Valid @RequestBody CreateProductRequest request
    );

    @GetMapping
    @Operation(summary = "내 가게의 모든 상품 목록 조회")
    List<ProductResponse> getAllProducts(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "id") Long storeId
    );

    @GetMapping("/{productId}")
    @Operation(summary = "특정 상품 정보 단일 조회")
    ProductResponse getProduct(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "id") Long storeId,
            @Parameter(description = "상품 ID", required = true, example = "1")
            @PathVariable Long productId
    );

    @PutMapping("/{productId}")
    @Operation(summary = "특정 상품 정보 수정")
    ProductResponse updateProduct(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "id") Long storeId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request
    );

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "특정 상품 삭제")
    void deleteProduct(
            @Parameter(hidden = true) @AuthenticationPrincipal(expression = "id") Long storeId,
            @PathVariable Long productId
    );
}