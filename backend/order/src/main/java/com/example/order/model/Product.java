package com.example.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Product (
    Integer id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    String category,

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate created
    ){}
