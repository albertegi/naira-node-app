package com.alvirg.ecommerce.product;

public record ProductPurchaseRequest(
        Integer productId,
        double quantity
) {
}
