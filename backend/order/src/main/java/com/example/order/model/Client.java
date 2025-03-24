package com.example.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Set;

public record Client(
        Integer id,
        String name,
        String email,
        String password,
        Set<String> roles,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate created
){}