package com.alvirg.ecommerce.order;

import com.alvirg.ecommerce.customer.CustomerClient;
import com.alvirg.ecommerce.customer.CustomerResponse;
import com.alvirg.ecommerce.kafka.OrderConfirmation;
import com.alvirg.ecommerce.kafka.OrderProducer;
import com.alvirg.ecommerce.order.exception.BusinessException;
import com.alvirg.ecommerce.orderline.OrderLineRequest;
import com.alvirg.ecommerce.orderline.OrderLineService;
import com.alvirg.ecommerce.payment.PaymentClient;
import com.alvirg.ecommerce.payment.PaymentRequest;
import com.alvirg.ecommerce.product.ProductCient;
import com.alvirg.ecommerce.product.PurchaseRequest;
import com.alvirg.ecommerce.product.PurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final OrderLineService orderLineService;

    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest request) {
        // check if we have the customer or not - use (OpenFeign) first create a customer client
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        // we need to purchase the products using product microservice use (Rest template)
        var purchasedProducts = productCient.purchaseProduct(request.products());


        // once we purchase the product we need to persist the order object
        var order = this.orderRepository.save(mapper.toOrder(request));

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

        // start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);
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

    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .toList();
    }

    public OrderResponse findById(Integer id) {
        return orderRepository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(()-> new EntityNotFoundException(
                        String.format("No order found with the provided ID: %d", id)
                ));
    }
}
