package com.vortexbird.paymentorders.auth.controller;

import com.vortexbird.paymentorders.auth.dto.LoginRequest;
import com.vortexbird.paymentorders.auth.dto.LoginResponse;
import com.vortexbird.paymentorders.auth.dto.UserProfileResponse;
import com.vortexbird.paymentorders.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

/**
 * Endpoints públicos de autenticación.
 */
@Tag(
        name = "Authentication",
        description = "Endpoints for user authentication and current user information."
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Authenticate user",
            description = "Validates credentials and returns a JWT token."
    )
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }
    @Operation(
            summary = "Get current authenticated user",
            description = "Returns information about the authenticated user extracted from the JWT token."
    )
    @GetMapping("/me")
    public UserProfileResponse me(
            Authentication authentication
    ) {

        return authService.getCurrentUser(
                authentication.getName()
        );
    }
}