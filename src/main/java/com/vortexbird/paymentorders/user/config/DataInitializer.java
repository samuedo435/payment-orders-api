package com.vortexbird.paymentorders.user.config;

import com.vortexbird.paymentorders.user.entity.Role;
import com.vortexbird.paymentorders.user.entity.User;
import com.vortexbird.paymentorders.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializa datos básicos necesarios para ejecutar
 * la aplicación en ambientes de desarrollo.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createAdminUser();
        createOperatorUser();
    }

    private void createAdminUser() {

        if (userRepository.existsByEmail("admin@vortexbird.com")) {
            return;
        }

        User admin = User.builder()
                .name("Administrator")
                .email("admin@vortexbird.com")
                .password(passwordEncoder.encode("123456"))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
    }

    private void createOperatorUser() {

        if (userRepository.existsByEmail("operator@vortexbird.com")) {
            return;
        }

        User operator = User.builder()
                .name("Operator")
                .email("operator@vortexbird.com")
                .password(passwordEncoder.encode("123456"))
                .role(Role.OPERATOR)
                .build();

        userRepository.save(operator);
    }
}