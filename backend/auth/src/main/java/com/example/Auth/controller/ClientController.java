package com.example.Auth.controller;

import com.example.Auth.model.Client;
import com.example.Auth.model.dto.ClientDTO;
import com.example.Auth.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService service;


    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAll() {
        List<Client> list = service.findAll();
        List<ClientDTO> clientDTO = list.stream().map(ClientDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(clientDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Integer id) {
        Client client = service.findById(id);
        return ResponseEntity.ok().body(new ClientDTO(client));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ClientDTO clientDTO) {
        Client client = service.create(clientDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).body("Client created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(@PathVariable Integer id, @RequestBody @Valid ClientDTO clientDTO) {
        Client client = service.update(id, clientDTO);
        return ResponseEntity.ok().body(new ClientDTO(client));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> delete(@PathVariable Integer id) {
//        try {
//            service.delete(id);
//            return ResponseEntity.ok("Id " + id + " deleted successfully");
//        }catch (ResponseStatusException ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getReason());
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Id " + id + " deleted successfully");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @PutMapping("/{clientId}/user/{userId}")
    public ResponseEntity<?> updateClientUserId(@PathVariable Integer clientId, @PathVariable Integer userId) {
        service.updateClientUserId(clientId, userId);
        return ResponseEntity.noContent().build();
    }
}
