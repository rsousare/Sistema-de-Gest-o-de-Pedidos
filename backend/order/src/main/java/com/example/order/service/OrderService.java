package com.example.order.service;

import com.example.order.model.Client;
import com.example.order.model.Order;
import com.example.order.model.Status;
import com.example.order.model.dto.OrderDTO;
import com.example.order.proxy.ClientProxy;
import com.example.order.repository.OrderItemRepository;
import com.example.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ClientProxy proxy;
    private final OrderItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository repository, ClientProxy proxy, OrderItemRepository itemRepository) {
        this.repository = repository;
        this.proxy = proxy;
        this.itemRepository = itemRepository;
    }

    public List<OrderDTO> getAll() {
        return repository.findAll().stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getTotalPrice(),
                        order.getCreated(),
                        order.getClientId()
                ))
                .collect(Collectors.toList());
    }

    public OrderDTO findOrderById(Integer id) {
        Optional<Order> order = repository.findById(id);
        if (order.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Id not found");
        }
        Order orderFound = order.get();
        if (orderFound.getClientId() != null) {
            try {
                Client client = proxy.getClientById(orderFound.getClientId());
                orderFound.setClient(client);
            }catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client Id not found");
            }
        }
        return new OrderDTO(
                orderFound.getId(),
                orderFound.getStatus(),
                orderFound.getTotalPrice(),
                orderFound.getCreated(),
                orderFound.getClientId()
        );
    }

    public Optional<OrderDTO> getOrderById(Integer id) {
        return repository.findById(id)
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getTotalPrice(),
                        order.getCreated(),
                        order.getClientId()
                ));
    }

    public Order create(Order order) {
        try {
            proxy.getClientById(order.getClientId());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client Id not found");
        }
        if (order.getStatus() == null) {
            order.setStatus(Status.PENDING);
        }
        if (order.getTotalPrice() == null) {
            order.setTotalPrice(BigDecimal.ZERO);
        }
        return repository.save(order);
    }

    public Order update(Order order) {

        Optional<Order> existingOrder = repository.findById(order.getId());
        if (existingOrder.isPresent()) {
            Order orderToUpdate = existingOrder.get();

            if (order.getClientId() != null) {
                try {
                    proxy.getClientById(order.getClientId());
                }catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client Id not found");
                }
            }

            orderToUpdate.setStatus(order.getStatus());
            orderToUpdate.setTotalPrice(order.getTotalPrice());
            orderToUpdate.setClientId(order.getClientId());

            return repository.save(orderToUpdate);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with id " + order.getId() + " not found!");
        }
    }

    public Order statusUpdate(Integer orderId, Status newStatus) {
        Optional<Order> existingOrder = repository.findById(orderId);
        if (existingOrder.isPresent()) {
            Order orderToUpdate = existingOrder.get();
            orderToUpdate.setStatus(newStatus);

            return repository.save(orderToUpdate);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with id " + orderId + " not found!");
        }
    }

    public void delete(Integer id) {
        Optional<Order> order = repository.findById(id);
        if (order.isPresent()) {
            if (itemRepository.existsByOrderId(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Order with id " + id + " has associated items and cannot be deleted");
            }
            repository.delete(order.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id " + id);
        }
    }

//    public boolean hasOrders(Integer userId) {
//        return repository.existsByUserId(userId);
//    }

    public boolean clientOrders(Integer clientId) {
        return repository.existsByClientId(clientId);
    }
}
