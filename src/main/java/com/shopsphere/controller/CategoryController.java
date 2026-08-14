package com.shopsphere.controller;


import com.shopsphere.dto.request.CategoryRequest;
import com.shopsphere.dto.response.CategoryResponse;
import com.shopsphere.model.Category;
import com.shopsphere.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest request) {

        CategoryResponse response = this.categoryService.createCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId){

        CategoryResponse response = this.categoryService.getCategoryById(categoryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){

        Page<CategoryResponse> responses = this.categoryService.getAllCategories(page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }


    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategoryById(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequest request){

        CategoryResponse response = this.categoryService.updateCategoryById(categoryId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PatchMapping("/{categoryId}/deactivate")
    public ResponseEntity<?> deactivateCategoryById(@PathVariable Long categoryId){
        this.categoryService.deactivateCategory(categoryId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId){

        this.categoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }

}
