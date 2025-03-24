package com.example.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate created;


    @Transient
    private Client client;
    private Integer clientId;

    @PrePersist
    public void date() {
        if (this.created == null) {
            this.created = LocalDate.now();
        }
    }

    public Order() {
    }

    public Order(Integer id, Status status, BigDecimal totalPrice, LocalDate created, Client client, Integer clientId) {
        this.id = id;
        this.status = status;
        this.totalPrice = totalPrice;
        this.created = created;
        this.client = client;
        this.clientId = clientId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(getId(), order.getId()) && getStatus() == order.getStatus() && Objects.equals(getTotalPrice(), order.getTotalPrice()) && Objects.equals(getCreated(), order.getCreated()) && Objects.equals(getClient(), order.getClient()) && Objects.equals(getClientId(), order.getClientId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStatus(), getTotalPrice(), getCreated(), getClient(), getClientId());
    }
}
