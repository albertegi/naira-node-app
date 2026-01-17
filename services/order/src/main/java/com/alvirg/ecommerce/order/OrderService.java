package com.alvirg.ecommerce.order;

import com.alvirg.ecommerce.customer.CustomerClient;
import com.alvirg.ecommerce.order.exception.BusinessException;
import com.alvirg.ecommerce.product.ProductCient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final ProductCient productCient;

    public Integer createOrder(OrderRequest request) {
        // check if we have the customer or not - use (OpenFeign) first create a customer client
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        // we need to purchase the products using product microservice use (Rest template)
        this.productCient.purchaseProduct(request.products());


        // once we purchase the product we need to persist the order object
        var order = this.orderRepository.save(this.mapper.toOrder(request));
        // persis the order line
        // start payment process - to do
        // send the order confirmation --> notification microservice(kafka)
        return null;
    }
}
