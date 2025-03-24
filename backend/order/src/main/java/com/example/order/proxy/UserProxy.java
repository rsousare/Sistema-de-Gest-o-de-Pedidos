package com.example.order.proxy;

import com.example.order.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserProxy {

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Integer id);
}
