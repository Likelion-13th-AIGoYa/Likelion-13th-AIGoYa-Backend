package kr.elroy.aigoya.category.controller;

import kr.elroy.aigoya.category.api.CategoryApi;
import kr.elroy.aigoya.category.domain.Category;
import kr.elroy.aigoya.category.dto.request.CreateCategoryRequest;
import kr.elroy.aigoya.category.dto.request.UpdateCategoryRequest;
import kr.elroy.aigoya.category.dto.response.CategoryResponse;
import kr.elroy.aigoya.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {
    private final CategoryService categoryService;

    @Override
    public List<CategoryResponse> getMyCategories(Long storeId) {
        List<Category> categories = categoryService.findCategoriesByStore(storeId);
        return categories.stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    public CategoryResponse createMyCategory(CreateCategoryRequest request, Long storeId) {
        Category newCategory = categoryService.createCategory(storeId, request.name());
        return CategoryResponse.from(newCategory);
    }

    @Override
    public void updateMyCategory(Long categoryId, UpdateCategoryRequest request, Long storeId) {
        categoryService.updateCategoryName(storeId, categoryId, request.name());
    }

    @Override
    public void deleteMyCategory(Long categoryId, Long storeId) {
        categoryService.deleteCategory(storeId, categoryId);
    }
}
