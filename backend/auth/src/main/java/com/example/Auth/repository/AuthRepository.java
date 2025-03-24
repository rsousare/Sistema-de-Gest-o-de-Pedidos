package com.example.Auth.repository;

import com.example.Auth.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Integer> {
    boolean existsByEmail(String email);

    Optional<Auth> findByEmail(String email);
}
