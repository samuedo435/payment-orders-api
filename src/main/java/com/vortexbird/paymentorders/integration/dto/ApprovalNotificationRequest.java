package com.vortexbird.paymentorders.integration.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Información enviada al sistema externo
 * cuando una orden es aprobada.
 */
public record ApprovalNotificationRequest(

        Long orderId,

        BigDecimal amount,

        String approvedBy,

        LocalDateTime approvedAt

) {
}