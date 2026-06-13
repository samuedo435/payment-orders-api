package com.vortexbird.paymentorders.history.dto;

import java.time.LocalDateTime;

public record OrderStatusHistoryResponse(

        String previousStatus,

        String newStatus,

        LocalDateTime changedAt,

        String changedBy

) {
}