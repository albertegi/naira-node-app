package com.alvirg.ecommerce.product;

import com.alvirg.ecommerce.category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String description;
    private double availableQuantity;
    private BigDecimal price; // when working with money, currency, pricing, it has a lot of methods and well optimized
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
