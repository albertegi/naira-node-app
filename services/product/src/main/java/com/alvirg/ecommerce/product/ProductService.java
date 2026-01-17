package com.alvirg.ecommerce.product;

import com.alvirg.ecommerce.exception.ProductPurchaseException;
import com.alvirg.ecommerce.product.request.ProductPurchaseRequest;
import com.alvirg.ecommerce.product.request.ProductRequest;
import com.alvirg.ecommerce.product.response.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Integer createProduct(ProductRequest request) {
        var product = mapper.toProduct(request);
        return repository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProduct(List<ProductPurchaseRequest> request) {
        // few checks
        // check if we have all the products
        // check available quantity for the products

        // first extract all product ids from the request object
        var productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        // check if all these products are available in my db
        var storedProducts = repository.findAllByIdInOrderById(productIds);

        // check that the list of product a customer is purchasing does not exceed the ones already in the db
        if(productIds.size() != storedProducts.size()){
            throw new ProductPurchaseException("One or more products does not exists");
        }

        var storedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>(); // start purchase different products
        for(int i = 0; i < storedRequest.size(); i++){
            var product = storedProducts.get(i); // available quantity
            var productRequest = storedRequest.get(i); // requested quantity

            if(product.getAvailableQuantity() < productRequest.quantity()){
                throw new ProductPurchaseException("Insufficient stock quantity for product with ID::" + productRequest.productId());
            }

            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);

            purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }

        // loop over list of products

        return null;
    }

    public ProductResponse findById(Integer productId) {
        return repository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(()-> new EntityNotFoundException("Product not found withe the ID::" + productId));
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
