package com.example.order.proxy;

import com.example.order.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product")
public interface ProductProxy {
    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable Integer id);

    @PutMapping("/products/{id}/update-stock")
    void updateStock(@PathVariable("id") Integer id, @RequestParam("newStock") int newStock);
}
