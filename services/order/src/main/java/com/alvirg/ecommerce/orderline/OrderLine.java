package com.alvirg.ecommerce.orderline;

import com.alvirg.ecommerce.order.Order;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "customer_line")
public class OrderLine {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer productId; // relationship with Product entity in Product domain
    private double quantity;

}
