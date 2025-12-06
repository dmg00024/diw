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

/**
 * Configuración de seguridad para la aplicación Spring Boot.
 *
 * <p>Provee beans para:
 * <ul>
 *   <li>Un {@link PasswordEncoder} basado en {@link BCryptPasswordEncoder}.</li>
 *   <li>Un {@link UserDetailsService} en memoria con dos usuarios de ejemplo
 *       (roles ADMIN y USER).</li>
 *   <li>Un {@link SecurityFilterChain} que define las reglas de autorización,
 *       permite el acceso público a los recursos de Swagger/OpenAPI y requiere
 *       autenticación para el resto de endpoints.</li>
 * </ul>
 *
 * <p>La clase está anotada con {@code @EnableWebSecurity} y {@code @EnableMethodSecurity}
 * para habilitar la seguridad web y las anotaciones de seguridad a nivel de método.
 *
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Crea y configura un {@link PasswordEncoder} que utiliza el algoritmo BCrypt.
     *
     * <p>BCrypt es recomendado para el almacenamiento seguro de contraseñas por su
     * resistencia a ataques por fuerza bruta y su uso de sal interna.
     *
     * @return un {@link PasswordEncoder} basado en BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proporciona un {@link UserDetailsService} en memoria con dos usuarios de ejemplo.
     *
     * <p>Los usuarios creados son:
     * <ul>
     *   <li>Usuario {@code admin} con contraseña codificada y rol {@code ADMIN}.</li>
     *   <li>Usuario {@code user} con contraseña codificada y rol {@code USER}.</li>
     * </ul>
     *
     * <p>Las contraseñas se codifican usando el {@link PasswordEncoder} inyectado.
     *
     * @param encoder el {@link PasswordEncoder} usado para codificar las contraseñas
     * @return un {@link InMemoryUserDetailsManager} con los usuarios configurados
     */
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

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     *
     * <p>Reglas principales:
     * <ul>
     *   <li>Permite acceso público a los recursos estáticos y endpoints relacionados con Swagger/OpenAPI
     *       para facilitar el UI de la documentación ({@code /swagger-ui/**}, {@code /v3/api-docs/**}, etc.).</li>
     *   <li>Requiere autenticación para cualquier otra petición.</li>
     *   <li>Habilita autenticación HTTP básica y desactiva CSRF (útil para APIs; revisar según necesidades).</li>
     * </ul>
     *
     * @param http el builder {@link HttpSecurity} provisto por Spring Security
     * @return la instancia construida de {@link SecurityFilterChain}
     * @throws Exception si ocurre un error durante la configuración del filtro de seguridad
     */
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