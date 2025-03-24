package com.example.Auth.security;

import com.example.Auth.model.dto.CredentialsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            CredentialsDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredentialsDTO.class);
            //System.out.println("Email: " + creds.getEmail() + ", Senha: " + creds.getPassword());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    creds.getEmail(), creds.getPassword(), new ArrayList<>());
            return authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException ex) {
            //System.out.println("Invalid Credentials " + ex.getMessage());
            throw ex;
        }catch (Exception e) {
            //System.out.println("Authentication " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String email = ((UserSecurity) authResult.getPrincipal()).getUsername();
        String name = ((UserSecurity) authResult.getPrincipal()).getName();
        //List<String> roles = Collections.singletonList("USER");
        List<String> roles = ((UserSecurity) authResult.getPrincipal()).getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String token = jwtUtil.generateToken(email, name, roles);
        //System.out.println("Token gerado: " + token);

        //Adiciona o token no Headers do Postman
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Authorization", "Bearer " + token);
        //Adiciona o token no corpo da resposta do Postman
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"Bearer " + token + "\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().append(json());
    }

    private CharSequence json() {
        long date = new Date().getTime();
        String response = "{"
                + "\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                +  "\"error\": \"Not Authorized\", "
                + "\"message\": \"Invalid email or password\", "
                + "\"path\": \"/login\"}";

        System.out.println(response);
        return response;
    }
}
