package com.shopsphere.service;

import com.shopsphere.dto.request.ProductRequest;
import com.shopsphere.dto.response.ProductResponse;
import com.shopsphere.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse getProductById(Long productId);

    Page<ProductResponse> getAllProducts(int page, int size);

    ProductResponse updateProduct(Long productId, ProductRequest productRequest);

    void deactivateProduct(Long productId);

    void activateProduct(Long productId);

//    Page<ProductResponse> searchProducts(String productName, int page, int size);

    void deleteProduct(Long productId);

    Page<ProductResponse> findByActiveTrueAndPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, int page, int size);

    Page<ProductResponse> searchProducts(
            String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size
    );

}
