package com.example.Auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record User(
    Integer id,

    String address,

    String phone,

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate birthDate
    ) {}
