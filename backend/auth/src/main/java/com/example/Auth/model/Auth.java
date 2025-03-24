package com.example.Auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Auth implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(nullable = false)
    protected String name;

    @Column(unique = true)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ROLE")
    protected Set<String> roles = new HashSet<>();

    @JsonFormat(pattern = "dd/MM/yyyy")
    protected LocalDate created;

    @PrePersist
    public void date() {
        if (this.created == null) {
            this.created = LocalDate.now();
        }
    }

    public Auth() {
        addRole(Role.ADMIN);
    }

    public Auth(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        addRole(Role.ADMIN);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Auth auth)) return false;
        return Objects.equals(getId(), auth.getId()) && Objects.equals(getName(), auth.getName()) && Objects.equals(getEmail(), auth.getEmail()) && Objects.equals(getPassword(), auth.getPassword()) && Objects.equals(getRoles(), auth.getRoles()) && Objects.equals(getCreated(), auth.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getPassword(), getRoles(), getCreated());
    }
}
