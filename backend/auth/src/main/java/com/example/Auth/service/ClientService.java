package com.example.Auth.service;

import com.example.Auth.model.Auth;
import com.example.Auth.model.Client;
import com.example.Auth.model.User;
import com.example.Auth.model.dto.ClientDTO;
import com.example.Auth.proxy.OrderProxy;
import com.example.Auth.proxy.UserProxy;
import com.example.Auth.repository.AuthRepository;
import com.example.Auth.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder encoder;
    private final OrderProxy orderProxy;
    private final UserProxy userProxy;

    @Autowired
    public ClientService(ClientRepository repository, AuthRepository authRepository, BCryptPasswordEncoder encoder, OrderProxy orderProxy, UserProxy userProxy) {
        this.repository = repository;
        this.authRepository = authRepository;
        this.encoder = encoder;
        this.orderProxy = orderProxy;
        this.userProxy = userProxy;
    }

    public Client findById(Integer id) {
        Optional<Client> client = repository.findById(id);
        return client.orElseThrow(()-> new EntityNotFoundException("Client not found with id: " + id));
    }

    public List<Client> findAll() {
        return repository.findAll();
    }

    public Client create(ClientDTO clientDTO) {
        clientDTO.setId(null);
        clientDTO.setPassword(encoder.encode(clientDTO.getPassword()));
        validaEmail(clientDTO);
        Client newClient = new Client(clientDTO);
        return  repository.save(newClient);
    }

    public Client update(Integer id, ClientDTO clientDTO) {
        clientDTO.setId(id);
        Client oldClient = findById(id);
        if (!clientDTO.getPassword().equals(oldClient.getPassword())) {
            clientDTO.setPassword(encoder.encode(clientDTO.getPassword()));
        }
        validaEmail(clientDTO);
        Client updatedClient = new Client(clientDTO);
        updatedClient.setCreated(oldClient.getCreated());

        return repository.save(updatedClient);
    }

//    public void delete(Integer id) {
//        Optional<Client> client = repository.findById(id);
//        if (client.isPresent()) {
//            repository.delete(client.get());
//        }else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found with id: " + id);
//        }
//    }

    public void delete(Integer id) {
        Optional<Client> client = repository.findById(id);
        if (client.isPresent()) {
            Client clientEntity = client.get();

            if (orderProxy.hasOrders(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Client with id " + id
                        + " has associated orders and cannot be deleted!");
            }

            Integer userId = clientEntity.getUserId();

            if (userId != null) {
                User user = userProxy.getUserById(userId);
                if (user != null) {
                    System.out.println("Deleting User with ID: {}" + user.id());
                    userProxy.deleteUserById(user.id());
                } else {
                    System.out.println("User not found for Client ID: {}" + id);
                }
            }

            repository.delete(clientEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " not found");
        }
    }


    private void validaEmail(ClientDTO clientDTO) {
        Optional<Auth> auth = authRepository.findByEmail(clientDTO.getEmail());
        if (auth.isPresent() && !Objects.equals(auth.get().getId(), clientDTO.getId())) {
            throw new DataIntegrityViolationException("Email already exists");
        }
    }

    public void updateClientUserId(Integer clientId, Integer userId) {
        Client client = repository.findById(clientId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Client id not found!"));
        client.setUserId(userId);
        repository.save(client);
    }
}
