package com.vortexbird.paymentorders.auth.dto;

/**
 * Información básica del usuario autenticado.
 */
public record UserProfileResponse(

        Long id,
        String name,
        String email,
        String role

) {
}