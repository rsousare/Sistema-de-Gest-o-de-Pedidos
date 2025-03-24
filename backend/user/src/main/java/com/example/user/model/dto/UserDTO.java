package com.example.user.model.dto;

import com.example.user.model.User;

import java.time.LocalDate;

public record UserDTO(Integer id, String address, String phone, LocalDate birthdate, Integer clientId, String clientName) {
    public UserDTO(User user) {
        this(user.getId(), user.getAddress(), user.getPhone(), user.getBirthDate(),
                user.getClientId(), user.getClient().name());
    }
}
