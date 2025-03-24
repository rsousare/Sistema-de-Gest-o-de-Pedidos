package com.example.user.service;

import com.example.user.model.Client;
import com.example.user.model.User;
import com.example.user.model.dto.UserDTO;
import com.example.user.proxy.ClientProxy;
import com.example.user.proxy.OrderProxy;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {


    private final UserRepository repository;
    private final ClientProxy proxy;
    private final OrderProxy orderProxy;

    @Autowired
    public UserService(UserRepository repository, ClientProxy proxy, OrderProxy orderProxy) {
        this.repository = repository;
        this.proxy = proxy;
        this.orderProxy = orderProxy;
    }


    public List<UserDTO> getAll() {
        return repository.findAll().stream()
                .map(user -> {
                    Client client = proxy.getClientById(user.getClientId());
                    String clientName = (client != null) ? client.name() : "No Client";
                    return new UserDTO(
                            user.getId(),
                            user.getAddress(),
                            user.getPhone(),
                            user.getBirthDate(),
                            user.getClientId(),
                            clientName
                    );
                })
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Integer id) {
        return repository.findById(id);
    }


    public User create(User user) {
        try {
            proxy.getClientById(user.getClientId());
        }catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client Id not found");
        }
        if (repository.existsByPhone(user.getPhone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }

        User savedUser = repository.save(user);
        proxy.updateClientUserId(user.getClientId(), savedUser.getId());
       return savedUser;
    }

    public User update(User user) {
        Optional<User> existingUser = repository.findById(user.getId());
        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();

            try {
                proxy.getClientById(user.getClientId());
            }catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client Id not found!");
            }

            if (repository.existsByPhone(user.getPhone()) && !userToUpdate.getPhone().equals(user.getPhone())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
            }

            userToUpdate.setAddress(user.getAddress());
            userToUpdate.setPhone(user.getPhone());
            userToUpdate.setBirthDate(user.getBirthDate());
            userToUpdate.setClientId(user.getClientId());

            User updateUser = repository.save(userToUpdate);

            proxy.updateClientUserId(user.getClientId(), updateUser.getId());

            return updateUser;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + user.getId() + " not found");
        }
    }


    public void delete(Integer id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            repository.delete(user.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id);
        }
    }

//    public void delete(Integer id) {
//        Optional<User> user = repository.findById(id);
//        if (user.isPresent()) {
//            if (orderProxy.hasOrders(id)) {
//                throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id " + id
//                        + " has associated orders and cannot be deleted.");
//            }
//            repository.delete(user.get());
//        }else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
//        }
//    }
}
