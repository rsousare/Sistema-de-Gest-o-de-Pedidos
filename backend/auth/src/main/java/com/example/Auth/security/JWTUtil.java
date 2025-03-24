package com.example.Auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@PropertySource("classpath:/application-token.properties")
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;



    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public String generateToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
public String generateToken(String email, String name, List<String> roles) {
    return Jwts.builder()
            .setSubject(email)
            .claim("name", name)
            .claim("roles", roles)
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
}


    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        }catch (Exception ex) {
            //System.out.println("Error getting token claims: " + ex.getMessage());
            logger.error("Error getting token claims: " + ex.getMessage());
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());

            boolean isValid = username != null && expirationDate != null && now.before(expirationDate);
            //System.out.println("Token is valid: " + isValid);
            logger.error("Token is valid: " + isValid);
            return isValid;
        }
        //System.out.println("Token claims are null.");
        logger.error("Token claims are null.");
        return false;
    }


    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return (claims != null) ? claims.getSubject() : null;
    }

    public List<String> getRoles(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.get("roles", List.class) : null;
    }
}
