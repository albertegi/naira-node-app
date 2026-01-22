package com.alvirg.ecommerce.notification;

import com.alvirg.ecommerce.kafka.order.OrderConfirmation;
import com.alvirg.ecommerce.kafka.payment.PaymentConfirmation;
import com.alvirg.ecommerce.notification.order.OrderConfirmation;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {

    private String id;
    private NotificationType type;
    private LocalDateTime NotificationDate;
    private OrderConfirmation orderConfirmation;
    private PaymentConfirmation paymentConfirmation;

}
