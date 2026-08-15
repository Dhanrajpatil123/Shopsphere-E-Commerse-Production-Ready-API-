package com.shopsphere.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message =  "Product name is required")
    @Size(min = 2, max = 50, message = "Product name must be between 2 and 150 characters")
    private String productName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull
    @Min(value = 0, message = "Stock quantity can not be negative")
    private Integer stockQuantity;

    @NotBlank
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    private String sku;

    @NotNull(message = "Category Id is required")
    @Positive(message = "Category Id must be positive")
    private Long categoryId;


}
