package com.vortexbird.paymentorders.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Datos requeridos para crear una orden de pago.
 */
public record CreateOrderRequest(

        @NotBlank
        String description,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount

) {
}