package com.alvirg.ecommerce.payment;

import com.alvirg.ecommerce.notification.NotificationProducer;
import com.alvirg.ecommerce.notification.PaymentNotificationRequest;
import com.alvirg.ecommerce.payment.request.PaymentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;


    public Integer createPayment(PaymentRequest request) {
        var payment = this.repository.save(mapper.toPayment(request));

        notificationProducer.sendNotification(
                new PaymentNotificationRequest(
                        request.orderReference(),
                        request.amount(),
                        request.paymentMethod(),
                        request.customer().firstname(),
                        request.customer().lastname(),
                        request.customer().email()
                )
        );
        // send a notification to the Notification microservice

        return payment.getId();
    }
}
