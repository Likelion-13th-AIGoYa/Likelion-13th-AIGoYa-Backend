package kr.elroy.aigoya.category.service;

import kr.elroy.aigoya.category.domain.Category;
import kr.elroy.aigoya.category.exception.CategoryInUseException;
import kr.elroy.aigoya.category.exception.CategoryNameAlreadyExistsException;
import kr.elroy.aigoya.category.exception.CategoryNotFoundException;
import kr.elroy.aigoya.category.repository.CategoryRepository;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public Category createCategory(Long storeId, String name) {
        if (categoryRepository.existsByStoreIdAndName(storeId, name)) {
            throw new CategoryNameAlreadyExistsException();
        }
        Store store = storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);

        Category newCategory = Category.builder()
                .store(store)
                .name(name)
                .build();

        return categoryRepository.save(newCategory);
    }

    @Transactional(readOnly = true)
    public List<Category> findCategoriesByStore(Long storeId) {
        return categoryRepository.findByStoreId(storeId);
    }

    @Transactional(readOnly = true)
    public Category findCategoryById(Long storeId, Long categoryId) {
        return getCategoryAndCheckOwnership(storeId, categoryId);
    }

    public void updateCategory(Long storeId, Long categoryId, String newName) {
        Category category = getCategoryAndCheckOwnership(storeId, categoryId);

        if (!category.getName().equals(newName) && categoryRepository.existsByStoreIdAndName(storeId, newName)) {
            throw new CategoryNameAlreadyExistsException();
        }

        category.update(newName);
    }

    public void deleteCategory(Long storeId, Long categoryId) {
        Category category = getCategoryAndCheckOwnership(storeId, categoryId);

        if (productRepository.existsByCategoryId(categoryId)) {
            throw new CategoryInUseException();
        }

        categoryRepository.delete(category);
    }

    private Category getCategoryAndCheckOwnership(Long storeId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        if (!Objects.equals(category.getStore().getId(), storeId)) {
            throw new CategoryNotFoundException();
        }
        return category;
    }
}
