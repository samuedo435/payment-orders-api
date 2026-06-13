package com.vortexbird.paymentorders.order.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ArchiveOrdersRequest(

        @NotNull
        LocalDateTime cutoffDate

) {
}