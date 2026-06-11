package com.vortexbird.paymentorders.storage.dto;

/**
 * Respuesta devuelta al cargar una factura.
 */
public record FileUploadResponse(

        String message,

        String fileName

) {
}