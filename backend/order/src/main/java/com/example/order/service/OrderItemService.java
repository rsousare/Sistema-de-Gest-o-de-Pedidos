package com.example.order.service;

import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.model.Product;
import com.example.order.model.dto.OrderItemDTO;
import com.example.order.proxy.ProductProxy;
import com.example.order.repository.OrderItemRepository;
import com.example.order.repository.OrderRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    private final OrderItemRepository repository;

    private final OrderRepository orderRepository;

    private final ProductProxy proxy;


    public OrderItemService(OrderItemRepository repository, OrderRepository orderRepository, ProductProxy proxy) {
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.proxy = proxy;
    }

    public List<OrderItemDTO> getAll() {
        return repository.findAll().stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getOrder().getId(),
                        item.getProductId()
                ))
                .collect(Collectors.toList());
    }


    public Optional<OrderItemDTO> findById(Integer id) {
        return repository.findById(id)
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getOrder().getId(),
                        item.getProductId()
                ));
    }

    public OrderItemDTO create(OrderItemDTO itemDTO) {
        Optional<Order> optionalOrder = orderRepository.findById(itemDTO.orderId());
        if (optionalOrder.isEmpty()) {
            throw new EntityNotFoundException("Order not found with id: " + itemDTO.orderId());
        }
        Order order = optionalOrder.get();

        Product product;
        try {
            product = proxy.getProductById(itemDTO.productId());
        }catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("Product not found with id: " + itemDTO.productId());
        }

        int requestedQuantity = itemDTO.quantity();
        if (product.stock() < requestedQuantity) {
            throw new IllegalArgumentException("Insufficient stock for product id: " + product.id() +
                    ". Available stock: " + product.stock() + ", requested: " + itemDTO.quantity());
        }

        int newStock = product.stock() - requestedQuantity;
        try {
            proxy.updateStock(product.id(), newStock);
        }catch (FeignException ex) {
            throw new RuntimeException("Error updating stock for product id: " + product.id());
        }

        BigDecimal totalPrice = product.price().multiply(BigDecimal.valueOf(itemDTO.quantity()));

        OrderItem item = new OrderItem();
        item.setQuantity(itemDTO.quantity());
        item.setPrice(totalPrice);
        item.setOrder(order);
        item.setProduct(product);
        item.setProductId(product.id());

        OrderItem createdItem = repository.save(item);

        BigDecimal newTotalPrice = order.getTotalPrice().add(totalPrice);
        order.setTotalPrice(newTotalPrice);
        orderRepository.save(order);

        return new OrderItemDTO(
                createdItem.getId(),
                createdItem.getQuantity(),
                createdItem.getPrice(),
                createdItem.getOrder().getId(),
                createdItem.getProduct().id()
        );
    }

//    public OrderItem update(OrderItem updateItem) {
//        Optional<OrderItem> existingItem = repository.findById(updateItem.getId());
//        if (existingItem.isEmpty()) {
//            throw new EntityNotFoundException("Order Item with id: " + updateItem.getId() + " not found");
//        }
//        OrderItem itemToUpdate = existingItem.get();
//        itemToUpdate.setQuantity(updateItem.getQuantity());
//        itemToUpdate.setPrice(updateItem.getPrice());
//
//        return repository.save(itemToUpdate);
//    }

    public OrderItemDTO update(Integer itemId, int newQuantity) {
        Optional<OrderItem> optionalOrderItem = repository.findById(itemId);
        if (optionalOrderItem.isEmpty()) {
            throw new EntityNotFoundException("Order Item with id " + itemId + " not found");
        }

        OrderItem orderItem = optionalOrderItem.get();

        Product product;
        try {
            product = proxy.getProductById(orderItem.getProductId());
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("Product not found with id: " + orderItem.getProductId());
        }

        int oldQuantity = orderItem.getQuantity();
        int quantityDifference = newQuantity - oldQuantity;

        int newStock = product.stock() - quantityDifference;
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock for product id: " + product.id());
        }

        try {
            proxy.updateStock(product.id(), newStock);
        } catch (FeignException ex) {
            throw new RuntimeException("Error updating inventory for product id: " + product.id());
        }

        BigDecimal newPrice = product.price().multiply(BigDecimal.valueOf(newQuantity));
        BigDecimal priceDifference = newPrice.subtract(orderItem.getPrice());

        orderItem.setQuantity(newQuantity);
        orderItem.setPrice(newPrice);
        repository.save(orderItem);

        Order order = orderItem.getOrder();
        BigDecimal newTotalPrice = order.getTotalPrice().add(priceDifference);
        order.setTotalPrice(newTotalPrice);
        orderRepository.save(order);

        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                order.getId(),
                product.id()
        );
    }

    public void delete(Integer id) {
        Optional<OrderItem> item = repository.findById(id);
        if (item.isPresent()) {
            repository.delete(item.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Item not found with id: " + id);
        }
    }

    public void returnProductToStock(Integer id) {
        Optional<OrderItem> item = repository.findById(id);
        if (item.isPresent()) {
            OrderItem orderItem = item.get();

            Product product;
            try {
                product = proxy.getProductById(orderItem.getProductId());
            }catch (FeignException.NotFound ex) {
                throw new EntityNotFoundException("Product not found with id: " + orderItem.getProductId());
            }

            try {
                proxy.updateStock(product.id(), product.stock() + orderItem.getQuantity());
            }catch (FeignException ex) {
                throw new RuntimeException("Error updating stock for product id: " + product.id(), ex);
            }
            repository.delete(orderItem);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Item not found with id: " + id);
        }
    }
}
