package com.vortexbird.paymentorders.auth.dto;

/**
 * Respuesta devuelta al autenticarse correctamente.
 */
public record LoginResponse(

        String token,
        String email,
        String role

) {
}