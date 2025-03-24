package com.example.order.repository;

import com.example.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    //boolean existsByUserId(Integer id);

    boolean existsByClientId(Integer id);
}
