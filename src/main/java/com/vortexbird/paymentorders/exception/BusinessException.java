package com.vortexbird.paymentorders.exception;

/**
 * Excepción utilizada para errores de reglas de negocio.
 *
 * Ejemplos:
 * - Aprobar una orden ya aprobada.
 * - Rechazar una orden que no está pendiente.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}