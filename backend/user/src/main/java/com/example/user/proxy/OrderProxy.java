package com.example.user.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order")
public interface OrderProxy {

    @GetMapping("/orders/user/{userId}/exists")
    boolean hasOrders(@PathVariable Integer userId);
}
