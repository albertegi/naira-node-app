package com.alvirg.ecommerce.orderline;

import com.alvirg.ecommerce.order.Order;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineMapper {


    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.id())
                .order(Order.builder()
                        .id(request.orderId())
                        .build()
                )
                .productId(request.productId())
                .quantity(request.quantity())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
