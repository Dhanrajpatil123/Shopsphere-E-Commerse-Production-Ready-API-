package com.shopsphere.service;

import com.shopsphere.dto.request.CategoryRequest;
import com.shopsphere.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse getCategoryById(Long categoryId);

    Page<CategoryResponse> getAllCategories(int page, int size);

    CategoryResponse updateCategoryById(Long categoryId, CategoryRequest categoryRequest);

    void deactivateCategory(Long categoryId);

    void deleteCategory(Long categoryId);
}
