package com.vortexbird.paymentorders.auth.controller;

import com.vortexbird.paymentorders.auth.dto.LoginRequest;
import com.vortexbird.paymentorders.auth.dto.LoginResponse;
import com.vortexbird.paymentorders.auth.dto.UserProfileResponse;
import com.vortexbird.paymentorders.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

/**
 * Endpoints públicos de autenticación.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }
    @GetMapping("/me")
    public UserProfileResponse me(
            Authentication authentication
    ) {

        return authService.getCurrentUser(
                authentication.getName()
        );
    }
}