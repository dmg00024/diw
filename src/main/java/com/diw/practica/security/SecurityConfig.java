// src/main/java/com/diw/practica/security/SecurityConfig.java
package com.diw.practica.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        var admin = User.withUsername("admin")
                .password(encoder.encode("adminpass"))
                .roles("ADMIN")
                .build();

        var user = User.withUsername("user")
                .password(encoder.encode("userpass"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Swagger/OpenAPI: permitir acceso público a los assets del UI
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui/index.html",
                                "/swagger-ui",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/webjars/**",
                                "/swagger-resources/**"
                        ).permitAll()
                        // Resto de endpoints requieren autenticación
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()); // para APIs; ajustar según necesidad

        return http.build();
    }
}