package com.vortexbird.paymentorders.order.dto;

public record ArchiveOrdersResponse(

        String processStatus,

        Integer archivedRecords

) {
}