package com.shopsphere.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id",  nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id",  nullable = false)
    private Product product;

    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

}
