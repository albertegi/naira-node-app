package com.alvirg.ecommerce.payment;

import com.alvirg.ecommerce.customer.CustomerResponse;
import com.alvirg.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
