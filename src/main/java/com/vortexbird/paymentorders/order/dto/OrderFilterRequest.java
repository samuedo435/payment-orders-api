package com.vortexbird.paymentorders.order.dto;

/**
 * Filtros disponibles para búsqueda de órdenes.
 */
public record OrderFilterRequest(

        String status,

        String createdBy

) {
}