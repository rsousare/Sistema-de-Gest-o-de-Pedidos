package com.example.Auth.service;

import com.example.Auth.model.Auth;
import com.example.Auth.model.Role;
import com.example.Auth.repository.AuthRepository;
import com.example.Auth.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JWTUtil jwtUtil;

    @Autowired
    public AuthService(AuthRepository repository, BCryptPasswordEncoder encoder, AuthenticationManager manager, JWTUtil jwtUtil) {
        this.repository = repository;
        this.encoder = encoder;
        this.manager = manager;
        this.jwtUtil = jwtUtil;
    }


    public List<Auth> getAll() {
        return repository.findAll();
    }

    public Optional<Auth> getAuthById(Integer id) {
        return repository.findById(id);
    }

    public Auth create(Auth auth) {

        if (repository.existsByEmail(auth.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        auth.setPassword(encoder.encode(auth.getPassword()));

        return repository.save(auth);
    }

    public Auth update(Auth auth) {
        Optional<Auth> existingAuth = repository.findById(auth.getId());
        if (existingAuth.isPresent()) {
            Auth updateAuth = existingAuth.get();
            updateAuth.setName(auth.getName());
            updateAuth.setEmail(auth.getEmail());
            updateAuth.setPassword(encoder.encode(auth.getPassword()));


            for (Role role : auth.getRoles()) {
                updateAuth.addRole(role);
            }

            return repository.save(updateAuth);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auth with id " + auth.getId() + " not found");
        }
    }

    public void delete(Integer id) {
        Optional<Auth> auth =repository.findById(id);
        if (auth.isPresent()) {
            repository.delete(auth.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id);
        }
    }
}
