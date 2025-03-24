package com.example.product.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductDTO(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Integer categoryId,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate created
        ) {
}
