package com.example.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotEmpty(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @NotEmpty(message = "Description is required")
    private String description;

    @Column(nullable = false)
    @DecimalMin(value = "00.1", message = "Price is required")
    private BigDecimal price;

    @Column(nullable = false)
    @Min(value = 0, message = "Stock is required")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDate created;

    @PrePersist
    public void date() {
        if (this.created == null) {
            this.created = LocalDate.now();
        }
    }

    public Product() {
    }

    public Product(Integer id, String name, String description, BigDecimal price, Integer stock, ProductCategory category, LocalDate created) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(getId(), product.getId()) && Objects.equals(getName(), product.getName()) && Objects.equals(getDescription(), product.getDescription()) && Objects.equals(getPrice(), product.getPrice()) && Objects.equals(getStock(), product.getStock()) && Objects.equals(getCategory(), product.getCategory()) && Objects.equals(getCreated(), product.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getPrice(), getStock(), getCategory(), getCreated());
    }
}
