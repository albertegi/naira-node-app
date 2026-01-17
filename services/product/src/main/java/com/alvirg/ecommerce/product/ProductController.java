package com.alvirg.ecommerce.product;

import com.alvirg.ecommerce.product.request.ProductPurchaseRequest;
import com.alvirg.ecommerce.product.request.ProductRequest;
import com.alvirg.ecommerce.product.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Integer> createProduct(
            @RequestBody @Valid
            ProductRequest request
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    // a method to purchase product: past a list of product purchase request, and return a list of product purchase response

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody List<ProductPurchaseRequest> request
    ){
        return ResponseEntity.ok(productService.purchaseProduct(request));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable("product-id")
            Integer productId){
        return ResponseEntity.ok(productService.findById(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(
            ){
        return ResponseEntity.ok(productService.findAll());
    }

}
