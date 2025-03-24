package com.example.order.proxy;

import com.example.order.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "auth")
public interface ClientProxy {

    @GetMapping("/clients/{id}")
    Client getClientById(@PathVariable Integer id);
}
