package com.alvirg.ecommerce.order;

import com.alvirg.ecommerce.customer.CustomerClient;
import com.alvirg.ecommerce.customer.CustomerResponse;
import com.alvirg.ecommerce.kafka.OrderConfirmation;
import com.alvirg.ecommerce.kafka.OrderProducer;
import com.alvirg.ecommerce.order.exception.BusinessException;
import com.alvirg.ecommerce.orderline.OrderLineRequest;
import com.alvirg.ecommerce.orderline.OrderLineService;
import com.alvirg.ecommerce.product.ProductCient;
import com.alvirg.ecommerce.product.PurchaseRequest;
import com.alvirg.ecommerce.product.PurchaseResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final ProductCient productCient;
    private OrderLineService orderLineService;

    private final OrderProducer orderProducer;

    public Integer createOrder(OrderRequest request) {
        // check if we have the customer or not - use (OpenFeign) first create a customer client
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        // we need to purchase the products using product microservice use (Rest template)
        var purchasedProducts = this.productCient.purchaseProduct(request.products());


        // once we purchase the product we need to persist the order object
        var order = this.orderRepository.save(this.mapper.toOrder(request));

        // persis the order line
        for(PurchaseRequest purchaseRequest: request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // todo start payment process
        // send the order confirmation --> notification microservice(kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .toList();

    }
}
