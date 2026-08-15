package com.shopsphere.service;

import com.shopsphere.dto.request.ProductRequest;
import com.shopsphere.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse getProductById(Long productId);

    Page<ProductResponse> getAllProducts(int page, int size);

    ProductResponse updateProduct(Long productId, ProductRequest productRequest);

    void deactivateProduct(Long productId);

    void activateProduct(Long productId);

    Page<ProductResponse> searchProducts(String productName, int page, int size);

    void deleteProduct(Long productId);



}
