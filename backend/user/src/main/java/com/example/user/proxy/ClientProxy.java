package com.example.user.proxy;

import com.example.user.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

//@FeignClient(name = "auth", url = "http://localhost:8200")
@FeignClient(name = "auth")
public interface ClientProxy {

    @GetMapping("/clients/{id}")
    Client getClientById(@PathVariable Integer id);

    @PutMapping("/clients/{clientId}/user/{userId}")
    void updateClientUserId(@PathVariable Integer clientId, @PathVariable Integer userId);
}
