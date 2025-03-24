package com.example.product.repository;

import com.example.product.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    boolean existsById(Integer id);
}
