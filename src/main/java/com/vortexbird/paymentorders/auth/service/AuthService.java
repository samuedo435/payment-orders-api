package com.vortexbird.paymentorders.auth.service;

import com.vortexbird.paymentorders.auth.dto.LoginRequest;
import com.vortexbird.paymentorders.auth.dto.LoginResponse;
import com.vortexbird.paymentorders.auth.dto.UserProfileResponse;
import com.vortexbird.paymentorders.security.jwt.JwtService;
import com.vortexbird.paymentorders.user.entity.User;
import com.vortexbird.paymentorders.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Gestiona los procesos de autenticación.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(
                        request.email())
                .orElseThrow();

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .authorities(
                                "ROLE_" + user.getRole().name()
                        )
                        .build()
        );

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
    public UserProfileResponse getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}