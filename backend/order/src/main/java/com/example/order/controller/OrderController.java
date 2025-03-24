package com.example.order.controller;

import com.example.order.model.Order;
import com.example.order.model.Status;
import com.example.order.model.dto.OrderDTO;
import com.example.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;


    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findOrderById(@PathVariable Integer id) {
        try {
            OrderDTO orderDTO = service.findOrderById(id);
            return ResponseEntity.ok(orderDTO);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getReason());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<OrderDTO> orders = service.getAll();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Empty list");
        }
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid Order order) {
        try {
            service.create(order);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getReason());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid Order order) {
        if (!id.equals(order.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID in the URL does not match the ID in the body");
        }
        Optional<OrderDTO> optionalOrder = service.getOrderById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        try {
            Order updateOrder = service.update(order);
            return ResponseEntity.ok(updateOrder);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getReason());
        }
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id, @RequestParam Status newStatus) {
        try {
            Order updateOrder = service.statusUpdate(id, newStatus);
            return ResponseEntity.ok(updateOrder);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Id " + id + " deleted successfully");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

//    @GetMapping("/user/{userId}/exists")
//    public boolean hasOrders(@PathVariable Integer userId) {
//        return service.hasOrders(userId);
//    }

    @GetMapping("/client/{clientId}/exists")
    public boolean clientOrders(@PathVariable Integer clientId) {
        return service.clientOrders(clientId);
    }
}
