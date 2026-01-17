package com.alvirg.ecommerce.product;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByIdInOrderById(List<Integer> productIds);
}
