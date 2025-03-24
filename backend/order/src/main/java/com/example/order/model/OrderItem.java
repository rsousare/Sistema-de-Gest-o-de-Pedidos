package com.example.order.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Transient
    private Product product;

    private Integer productId;

    public OrderItem() {
    }

    public OrderItem(Integer id, Order order, Integer quantity, BigDecimal price, Product product, Integer productId) {
        this.id = id;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
        this.productId = productId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem item)) return false;
        return Objects.equals(getId(), item.getId()) && Objects.equals(getOrder(), item.getOrder()) && Objects.equals(getQuantity(), item.getQuantity()) && Objects.equals(getPrice(), item.getPrice()) && Objects.equals(product, item.product) && Objects.equals(getProductId(), item.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrder(), getQuantity(), getPrice(), product, getProductId());
    }
}
