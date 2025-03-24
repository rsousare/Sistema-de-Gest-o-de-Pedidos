package com.example.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotEmpty(message = "You must fill in the address")
    private String address;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{9}$", message = "The phone must be 9 digits long")
    private String phone;

    @NotNull(message = "What is your date of birth")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past(message = "The date of birth must be in the past")
    private LocalDate birthDate;

    @Transient
    private Client client;

    private Integer clientId;

    public User() {
    }

    public User(Integer id, String address, String phone, LocalDate birthDate, Integer clientId) {
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.birthDate = birthDate;
        this.clientId = clientId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getAddress(), user.getAddress()) && Objects.equals(getPhone(), user.getPhone()) && Objects.equals(getBirthDate(), user.getBirthDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAddress(), getPhone(), getBirthDate());
    }
}
