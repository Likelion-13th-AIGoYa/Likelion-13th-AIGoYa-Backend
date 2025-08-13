package kr.elroy.aigoya.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.product.dto.request.CreateProductRequest;
import kr.elroy.aigoya.product.dto.response.ProductResponse;
import kr.elroy.aigoya.product.dto.request.UpdateProductRequest;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "나의 상품 API", description = "나의 상품 API")
@Validated
@RequestMapping("/v1/stores/me/products")
public interface MyProductApi {

    @Operation(summary = "나의 상품 목록 조회", description = "현재 토큰 주인의 가게 상품 목록을 조회합니다.")
    @GetMapping
    List<ProductResponse> getMyProducts(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 상품 조회", description = "현재 토큰 주인의 특정 상품 정보를 조회합니다.")
    @GetMapping("/{id}")
    ProductResponse getMyProduct(
            @Parameter(description = "상품 ID", required = true, example = "1")
            @PathVariable
            Long id,
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 상품 생성", description = "현재 토큰 주인의 가게에 새로운 상품을 생성합니다.")
    @PostMapping
    ProductResponse createMyProduct(
            @Valid
            @RequestBody
            CreateProductRequest request,
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 상품 수정", description = "현재 토큰 주인의 상품 정보를 수정합니다.")
    @PutMapping("/{id}")
    ProductResponse updateMyProduct(
            @Parameter(description = "상품 ID", required = true, example = "1")
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            UpdateProductRequest request,
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 상품 삭제", description = "현재 토큰 주인의 상품을 삭제합니다.")
    @DeleteMapping("/{id}")
    void deleteMyProduct(
            @Parameter(description = "상품 ID", required = true, example = "1")
            @PathVariable
            Long id,
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );
}
