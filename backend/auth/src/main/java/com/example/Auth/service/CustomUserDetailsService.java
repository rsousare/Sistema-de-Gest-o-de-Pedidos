package com.example.Auth.service;

import com.example.Auth.repository.AuthRepository;
import com.example.Auth.security.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .map(user-> new UserSecurity(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRoles()))
                .orElseThrow(()-> new UsernameNotFoundException(email));
    }

}
