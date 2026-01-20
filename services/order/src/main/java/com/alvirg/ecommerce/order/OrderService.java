package com.alvirg.ecommerce.order;

import com.alvirg.ecommerce.customer.CustomerClient;
import com.alvirg.ecommerce.order.exception.BusinessException;
import com.alvirg.ecommerce.orderline.OrderLineRequest;
import com.alvirg.ecommerce.orderline.OrderLineService;
import com.alvirg.ecommerce.product.ProductCient;
import com.alvirg.ecommerce.product.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final ProductCient productCient;
    private OrderLineService orderLineService;

    public Integer createOrder(OrderRequest request) {
        // check if we have the customer or not - use (OpenFeign) first create a customer client
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        // we need to purchase the products using product microservice use (Rest template)
        this.productCient.purchaseProduct(request.products());


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
        return null;
    }
}
