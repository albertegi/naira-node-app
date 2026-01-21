package com.alvirg.ecommerce.payment.request;

import com.alvirg.ecommerce.payment.PaymentMethod;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;

import java.math.BigDecimal;

public record PaymentRequest(

        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        Customer customer
) {
}
