package com.vortexbird.paymentorders.exception;

/**
 * Excepción utilizada cuando un recurso solicitado
 * no existe en el sistema.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}