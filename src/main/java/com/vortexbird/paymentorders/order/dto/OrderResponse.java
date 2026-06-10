package com.vortexbird.paymentorders.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Información expuesta al cliente
 * sobre una orden de pago.
 */
public record OrderResponse(

        Long id,

        String description,

        BigDecimal amount,

        String status,

        String createdBy,

        String approvedBy,

        String rejectedBy,

        String invoicePath,

        LocalDateTime createdAt,

        LocalDateTime approvedAt,

        LocalDateTime rejectedAt

) {
}