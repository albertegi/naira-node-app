package com.alvirg.ecommerce.payment;

import com.alvirg.ecommerce.payment.request.Customer;
import com.alvirg.ecommerce.payment.request.PaymentRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentMapper {
    public Payment toPayment(PaymentRequest request) {

        return Payment.builder()
                .id(request.id())
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .orderId(request.orderId())
                .build();
    }
}
