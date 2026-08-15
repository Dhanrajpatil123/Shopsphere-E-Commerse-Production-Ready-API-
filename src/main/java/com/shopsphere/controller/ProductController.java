package com.shopsphere.controller;

import com.shopsphere.dto.request.ProductRequest;
import com.shopsphere.dto.response.ProductResponse;
import com.shopsphere.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest){
        ProductResponse response = this.productService.createProduct(productRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }



    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId){
        ProductResponse response = this.productService.getProductById(productId);

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }



    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size){

        Page<ProductResponse> responses = this.productService.getAllProducts(page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }


    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductRequest productRequest){

        ProductResponse response = this.productService.updateProduct(productId, productRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PatchMapping("/{productId}/deactivate")
    public ResponseEntity<?> deactivateProduct(@PathVariable Long productId){

        this.productService.deactivateProduct(productId);

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{productId}/activate")
    public ResponseEntity<?> activateProduct(@PathVariable Long productId){

        this.productService.activateProduct(productId);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestParam String productName,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size){

        Page<ProductResponse> responses = this.productService.searchProducts(productName, page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId){
        this.productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

}
