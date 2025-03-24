package com.example.Auth.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order")
public interface OrderProxy {

    @GetMapping("/orders/client/{clientId}/exists")
    boolean hasOrders(@PathVariable Integer clientId);
}
