package kr.elroy.aigoya.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.product.dto.CreateProductRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 API", description = "상품 API")
@RestController
@Validated
@RequestMapping("/v1/products")
public interface ProductApi {

    @Operation(summary = "상품 조회", description = "상품 정보를 조회합니다.")
    @GetMapping("/{id}")
    ProductResponse getProduct(
            @Parameter(description = "상품 ID", required = true, example = "1")
            @PathVariable
            Long id
    );

    @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
    @PostMapping("/create")
    ProductResponse createProduct(
            @Valid
            @Parameter(description = "상품 정보", required = true)
            CreateProductRequest request
    );
}