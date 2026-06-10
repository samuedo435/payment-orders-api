package com.vortexbird.paymentorders.exception;

import java.time.LocalDateTime;

/**
 * DTO estándar para respuestas de error.
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {
}