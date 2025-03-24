package com.example.Auth.proxy;

import com.example.Auth.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserProxy {

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Integer id);

    @DeleteMapping("/users/{id}")
    void deleteUserById(@PathVariable Integer id);
}
