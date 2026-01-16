package com.alvirg.ecommerce.product;

import com.alvirg.ecommerce.category.Category;
import com.alvirg.ecommerce.product.request.ProductRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest request) {
        return Product.builder()
                .id(request.id())
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .category(Category.builder()
                        .id(request.categoryId())
                        .build())
                .build();
    }
}
