package com.example.Auth.config;

import com.example.Auth.security.JWTAuthenticationFilter;
import com.example.Auth.security.JWTAuthorizationFilter;
import com.example.Auth.security.JWTUtil;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:/application-token.properties")
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;


    private final Environment env;

    private final JWTUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(Environment env, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.env = env;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;

        if (env.getActiveProfiles().length > 0) {
            System.out.println("Active profiles: " + Arrays.toString(env.getActiveProfiles()));
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception{
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers(headers-> headers.frameOptions(frameOptions-> frameOptions.disable()));
        }

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authManager, jwtUtil);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter(authManager, jwtUtil, userDetailsService);

        http.csrf(csrf-> csrf.disable())
                //.cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers( HttpMethod.POST, "/auths", "/clients").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auths/**").authenticated()
                        .anyRequest().authenticated())
                .sessionManagement(session-> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .oauth2ResourceServer(oauth2-> oauth2.jwt(jwt-> jwt.decoder(jwtDecoder())))

                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthenticationFilter);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return NimbusJwtDecoder.withSecretKey(Keys.hmacShaKeyFor(keyBytes)).build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
