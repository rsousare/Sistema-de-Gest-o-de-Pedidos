package com.example.Auth.model;

import com.example.Auth.model.dto.ClientDTO;
import jakarta.persistence.Entity;

import java.util.stream.Collectors;

@Entity
public class Client extends Auth {

    protected Integer userId;

    public Client() {
        addRole(Role.USER);
    }

    public Client(Integer id, String name, String email, String password) {
        super(id, name, email, password);
        addRole(Role.USER);
    }

    public Client(ClientDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.roles = dto.getRoles().stream().map(Role::getDescription).collect(Collectors.toSet());
        this.created = dto.getCreated();
        this.userId = dto.getUserId();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
