package com.shopsphere.serviceImplementation;

import com.shopsphere.dto.request.CategoryRequest;
import com.shopsphere.dto.response.CategoryResponse;
import com.shopsphere.exception.CategoryAlreadyDeactivatedException;
import com.shopsphere.exception.DuplicateResourceException;
import com.shopsphere.exception.ResourceNotFoundException;
import com.shopsphere.model.Category;
import com.shopsphere.repository.CategoryRepository;
import com.shopsphere.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation implements CategoryService {


    private final CategoryRepository categoryRepository;



    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {

        if (this.categoryRepository.existsByCategoryNameIgnoreCase(categoryRequest.getCategoryName())) {
            throw new DuplicateResourceException("Category already exists with name: " + categoryRequest.getCategoryName());
        }

        Category category = Category.builder()
                .categoryName(categoryRequest.getCategoryName())
                .description(categoryRequest.getDescription())
                .active(true)
                .build();

        this.categoryRepository.save(category);

        return mapToResponse(category);
    }



    @Override
    public CategoryResponse getCategoryById(Long categoryId) {

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with Id : " + categoryId));

        return mapToResponse(category);
    }


    @Override
    public Page<CategoryResponse> getAllCategories(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

//        Page<Category> categories = this.categoryRepository.findAll(pageable);

        Page<Category> categories = this.categoryRepository.findByActiveTrue(pageable);

        return categories.map(this::mapToResponse);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public CategoryResponse updateCategoryById(Long categoryId, CategoryRequest categoryRequest) {

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with Id : "+ categoryId));


        if (!category.getCategoryName().equalsIgnoreCase(categoryRequest.getCategoryName())
                && this.categoryRepository.existsByCategoryNameIgnoreCase(categoryRequest.getCategoryName())){

            throw new DuplicateResourceException("Category already exists with name: " + categoryRequest.getCategoryName());
        }


        category.setCategoryName(categoryRequest.getCategoryName());
        category.setDescription(categoryRequest.getDescription());
        category.setUpdatedAt(category.getUpdatedAt());

        Category savedCategory = this.categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deactivateCategory(Long categoryId) {

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with Id : " + categoryId));

        if (!category.isActive()){
            throw new CategoryAlreadyDeactivatedException(
                    "Category is already deactivated");
        }

        category.setActive(false);
        category.setUpdatedAt(category.getUpdatedAt());

        this.categoryRepository.save(category);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteCategory(Long categoryId) {

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with Id : "+ categoryId));


        this.categoryRepository.delete(category);
    }


    private CategoryResponse mapToResponse(Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .active(category.isActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }


}
