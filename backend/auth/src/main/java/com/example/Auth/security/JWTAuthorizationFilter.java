package com.example.Auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            UsernamePasswordAuthenticationToken authToken = getAuthentication(header.substring(7));
            if (authToken != null) {
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else {
                System.out.println("Invalid or expired token");
            }
        }else {
            System.out.println("Authorization header not found or does not start with 'Bearer'.");
        }
        chain.doFilter(request, response);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (!jwtUtil.isTokenValid(token)) {
            logger.warn("Invalid or expired token.");
            return null;
        }

        String username = jwtUtil.getUsername(token);
        List<String> roles = jwtUtil.getRoles(token);
        UserDetails details = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }
}
