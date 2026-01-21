package com.alvirg.ecommerce.kafka;

import com.alvirg.ecommerce.customer.CustomerResponse;
import com.alvirg.ecommerce.order.PaymentMethod;
import com.alvirg.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customerResponse, // this will contain the customer information
        List<PurchaseResponse> product// The list of the products the customer has purchased

        // All these information will first be sent to OrderService and OrderService will send to the broker
) {
}
