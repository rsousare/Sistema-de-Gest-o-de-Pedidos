package com.example.order.controller;

import com.example.order.model.OrderItem;
import com.example.order.model.dto.OrderItemDTO;
import com.example.order.proxy.ProductProxy;
import com.example.order.repository.OrderItemRepository;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/items")
public class OrderItemController {

    private final OrderItemService service;

    private final ProductProxy proxy;

    private final OrderItemRepository repository;

    private final OrderRepository orderRepository;

    public OrderItemController(OrderItemService service, ProductProxy proxy, OrderItemRepository repository, OrderRepository orderRepository) {
        this.service = service;
        this.proxy = proxy;
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<OrderItemDTO> items = service.getAll();
        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Empty list");
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<OrderItemDTO> item = service.findById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Item not found with id: " + id);
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> findOrderItemById(@PathVariable Integer id) {
//        var order = repository.findById(id);
//
//        if (order.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Item id not found");
//        }
//
//        var orderFound = order.get();
//
//        if (orderFound.getProductId() != null) {
//            proxy.getProductById(orderFound.getProductId());
//        }
//        var orderItemDto = new OrderItemDTO(
//                orderFound.getId(),
//                orderFound.getQuantity(),
//                orderFound.getPrice(),
//                orderFound.getOrder().getId(),
//                orderFound.getProductId()
//        );
//        return ResponseEntity.ok(orderItemDto);
//    }



    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid OrderItemDTO itemDTO) {
        try {
            OrderItemDTO created = service.create(itemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }



//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid OrderItem item) {
//        if (!id.equals(item.getId())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID in the URL does not match the ID in the body");
//        }
//        Optional<OrderItemDTO> optionalItem = service.findById(id);
//        if (optionalItem.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Item not found!");
//        }
//        OrderItem updateItem = service.update(item);
//        return ResponseEntity.ok(updateItem);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid OrderItemDTO itemDTO) {
        if (!id.equals(itemDTO.id())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID in the URL does not match the ID in the body");
        }

        Optional<OrderItemDTO> optionalItem = service.findById(id);
        if (optionalItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Item not found!");
        }

        try {
            OrderItemDTO updatedItem = service.update(id, itemDTO.quantity());
            return ResponseEntity.ok(updatedItem);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Id " + id + " deleted successfully");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getReason());
        }
    }

    @DeleteMapping("stock/{id}")
    public ResponseEntity<String> returnProductToStock(@PathVariable Integer id) {
        try {
            service.returnProductToStock(id);
            return ResponseEntity.ok("Order Item with id " + id + " deleted and product returned to stock");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getReason());
        }
    }
}
