package com.shopsphere.serviceImplementation;

import com.shopsphere.dto.request.ProductRequest;
import com.shopsphere.dto.response.ProductResponse;
import com.shopsphere.exception.DuplicateResourceException;
import com.shopsphere.exception.ResourceNotFoundException;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.repository.CategoryRepository;
import com.shopsphere.repository.ProductRepository;
import com.shopsphere.service.ProductService;
import com.shopsphere.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {


    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;



    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {

        //1. check stock keeping unit
        if(this.productRepository.existsBySkuIgnoreCase(productRequest.getSku())){
            throw new DuplicateResourceException("Product already exists with SKU: " + productRequest.getSku());
        }


        //2. find category
        Category category = this.categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productRequest.getCategoryId()));


        //3. Build product
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .sku(productRequest.getSku())
                .active(true)
                .category(category)
                .build();

        Product saveProduct = this.productRepository.save(product);

        return mapToResponse(saveProduct);
    }



    @Override
    public ProductResponse getProductById(Long productId) {

        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));


        return mapToResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = this.productRepository.findAll(pageable);

        return productPage.map(this::mapToResponse);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {

        // 1. Find existing product
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // 2. Check SKU only if SKU is changed
        if(!productRequest.getSku().equalsIgnoreCase(product.getSku())
                && this.productRepository.existsBySkuIgnoreCase(productRequest.getSku())){

            throw new DuplicateResourceException(
                    "Product already exists with SKU: "
                            + productRequest.getSku());
        }


        // 3. Find category
        Category category = this.categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: "
                        + productRequest.getCategoryId()));


        // 4. Update product fields
        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setSku(productRequest.getSku());
        product.setCategory(category);
        product.setUpdatedAt(LocalDateTime.now());

        Product updateProduct = this.productRepository.save(product);

        return mapToResponse(updateProduct);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deactivateProduct(Long productId) {

        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));


        if(!product.isActive())
            throw new IllegalStateException("Product is already deactivated");

        product.setActive(false);
        product.setUpdatedAt(LocalDateTime.now());

        this.productRepository.save(product);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void activateProduct(Long productId) {

        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.isActive())
            throw new IllegalStateException("Product is already active");

        product.setActive(true);
        product.setUpdatedAt(LocalDateTime.now());

        this.productRepository.save(product);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteProduct(Long productId) {

        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        this.productRepository.delete(product);
    }

    @Override
    public Page<ProductResponse> findByActiveTrueAndPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        if(minPrice.compareTo(maxPrice) > 0){
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = this.productRepository.findByActiveTrueAndPriceBetween(minPrice, maxPrice, pageable);

        return products.map(this::mapToResponse);
    }



//    @Override
//    public Page<ProductResponse> searchProducts(String productName, int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        Page<Product> productPage = this.productRepository.findByActiveTrueAndProductNameContainingIgnoreCase(productName, pageable);
//
//        return productPage.map(this::mapToResponse);
//    }



    @Override
    public Page<ProductResponse> searchProducts(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice,
                                                int page, int size) {


        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        Pageable pageable = PageRequest.of(page, size);

        Specification<Product> specification = ProductSpecification.isActive();

        if (keyword != null && !keyword.trim().isEmpty()) {

            specification = specification.and(ProductSpecification.hasKeyword(keyword.trim()));
        }

        if (categoryId != null) {

            specification = specification.and(ProductSpecification.hasCategory(categoryId));
        }


        if (minPrice != null) {

            specification = specification.and(ProductSpecification.priceGreaterThanOrEqualTo(minPrice));
        }

        if (maxPrice != null) {

            specification = specification.and(ProductSpecification.priceLessThanOrEqualTo(maxPrice)
            );
        }

        Page<Product> products = productRepository.findAll(specification, pageable);

        return products.map(this::mapToResponse);
    }








    private ProductResponse mapToResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .sku(product.getSku())
                .active(product.isActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getCategoryName())
                .build();
    }
}
