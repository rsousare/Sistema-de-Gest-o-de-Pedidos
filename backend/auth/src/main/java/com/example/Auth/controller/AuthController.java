package com.example.Auth.controller;

import com.example.Auth.model.Auth;
import com.example.Auth.repository.AuthRepository;
import com.example.Auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/auths")
public class AuthController {

    private final AuthService service;

    private final AuthRepository repository;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AuthController(AuthService service, AuthRepository repository, BCryptPasswordEncoder encoder) {
        this.service = service;
        this.repository = repository;
        this.encoder = encoder;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Auth> auths = service.getAll();
        if (auths.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Empty list");
        }
        return ResponseEntity.ok(auths);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthById(@PathVariable Integer id) {
        Optional<Auth> optionalAuth = service.getAuthById(id);
        if (optionalAuth.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found");
        }
        Auth auth = optionalAuth.get();
        return ResponseEntity.ok(auth);
    }


    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid Auth auth) {
        try {
            service.create(auth);
            return ResponseEntity.status(HttpStatus.CREATED).body("Auth created successfully");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getReason());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid Auth auth) {
        if (!id.equals(auth.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID in the URL does not match the ID in the body");
        }
        Optional<Auth> optionalAuth = service.getAuthById(id);
        if (optionalAuth.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auth not found");
        }
        if (repository.existsByEmail(auth.getEmail())) {
            Optional<Auth> existingAuth = repository.findByEmail(auth.getEmail());
            if (existingAuth.isPresent() && !existingAuth.get().getId().equals(auth.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
        }
        Auth authUpdate = service.update(auth);
        return ResponseEntity.ok(authUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Id " + id + " deleted successfully");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getReason());
        }
    }


}
