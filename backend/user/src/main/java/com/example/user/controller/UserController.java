package com.example.user.controller;

import com.example.user.model.User;
import com.example.user.model.dto.UserDTO;
import com.example.user.proxy.ClientProxy;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ClientProxy clientProxy;

    @Autowired
    private UserRepository repository;


    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Integer id) {
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Id not found");
        }

        User userFound = user.get();

        if (userFound.getClientId() != null) {
            var auth = clientProxy.getClientById(userFound.getClientId());
            userFound.setClient(auth);
        }
        return ResponseEntity.ok(userFound);
    }



    @GetMapping
    public ResponseEntity<?> getAll() {
        List<UserDTO> users = service.getAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Empty List");
        }
        return ResponseEntity.ok(users);
    }


    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid User user) {
        try {
            service.create(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid User user) {
        try {
            if (!id.equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID in the URL does not match the ID in the body");
            }
            Optional<User> optionalUser = service.getUserById(id);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            if (user.getBirthDate().isAfter(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verify your birthdate");
            }
            User updateUser = service.update(user);
            return ResponseEntity.ok(updateUser);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Id " + id + " deleted successfully");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }
}
