package com.vortexbird.paymentorders.security.config;

import com.vortexbird.paymentorders.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

/**
 * Configuración principal de Spring Security.
 *
 * Define:
 * - Endpoints públicos
 * - Roles
 * - JWT
 * - Estrategia stateless
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))

                .authorizeHttpRequests(auth -> auth

                        /*
                         * Endpoints públicos.
                         */
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        /*
                         * Creación de órdenes:
                         * solo OPERATOR.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/orders"
                        )
                        .hasRole("OPERATOR")

                        /*
                         * Aprobación y rechazo:
                         * solo ADMIN.
                         */
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/orders/*/approve"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/orders/*/reject"
                        )
                        .hasRole("ADMIN")

                        /*
                         * Consulta de órdenes:
                         * ADMIN y OPERATOR.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/orders/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERATOR"
                        )

                        .anyRequest()
                        .authenticated()
                )

                .httpBasic(Customizer.withDefaults())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }
}