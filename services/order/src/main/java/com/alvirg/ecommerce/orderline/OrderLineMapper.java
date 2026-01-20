package com.alvirg.ecommerce.orderline;

import com.alvirg.ecommerce.order.Order;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineMapper {

    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String productId; // relationship with Product entity in Product domain
    private double quantity;

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
}
