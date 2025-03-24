package com.example.order.model.dto;

import com.example.order.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderDTO(
        Integer id,
        Status status,
        BigDecimal totalPrice,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate created,

        Integer clientId
                       ) {
}
