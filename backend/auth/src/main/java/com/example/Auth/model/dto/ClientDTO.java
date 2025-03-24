package com.example.Auth.model.dto;

import com.example.Auth.model.Auth;
import com.example.Auth.model.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO implements Serializable {
    protected Integer id;

    @NotNull(message = "Name is required")
    protected String name;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    protected String email;

    @NotNull(message = "Password is required")
    protected String password;

    protected Set<String> roles = new HashSet<>();

    @JsonFormat(pattern = "dd/MM/yyyy")
    protected LocalDate created;

    protected Integer userId;

    public ClientDTO() {
        addRole(Role.USER);
    }

    public ClientDTO(Auth auth) {
        this.id = auth.getId();
        this.name = auth.getName();
        this.email = auth.getEmail();
        this.password = auth.getPassword();
        this.roles = auth.getRoles().stream().map(Role::getDescription).collect(Collectors.toSet());
        this.created = auth.getCreated();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles.stream().map(Role::toEnum).collect(Collectors.toSet());
    }

    public void addRole(Role role) {
        this.roles.add(role.getDescription());
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
