package kr.elroy.aigoya.category.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.elroy.aigoya.category.dto.request.CreateCategoryRequest;
import kr.elroy.aigoya.category.dto.request.UpdateCategoryRequest;
import kr.elroy.aigoya.category.dto.response.CategoryResponse;
import kr.elroy.aigoya.store.config.CurrentStoreId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Tag(name = "나의 카테고리 API", description = "나의 상품 카테고리 관리 API")
@RequestMapping("/v1/stores/me/categories")
public interface CategoryApi {
    @Operation(summary = "나의 모든 카테고리 조회")
    @GetMapping
    List<CategoryResponse> getMyCategories(
            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 특정 카테고리 조회")
    @GetMapping("/{categoryId}")
    CategoryResponse getMyCategory(
            @Parameter(description = "조회할 카테고리 ID", required = true)
            @PathVariable
            Long categoryId,

            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 카테고리 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryResponse createMyCategory(
            @Valid
            @RequestBody
            CreateCategoryRequest request,

            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 카테고리 수정")
    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateMyCategory(
            @Parameter(description = "수정할 카테고리 ID", required = true)
            @PathVariable
            Long categoryId,

            @Valid
            @RequestBody
            UpdateCategoryRequest request,

            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );

    @Operation(summary = "나의 카테고리 삭제")
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMyCategory(
            @Parameter(description = "삭제할 카테고리 ID", required = true)
            @PathVariable
            Long categoryId,

            @CurrentStoreId
            @Parameter(hidden = true)
            Long storeId
    );
}
