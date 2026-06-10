package com.vortexbird.paymentorders.user.entity;

/**
 * Roles disponibles dentro del sistema.
 *
 * ADMIN: Puede aprobar y rechazar órdenes.
 *
 * OPERATOR: Puede crear órdenes y gestionar facturas.
 */
public enum Role {
    ADMIN,
    OPERATOR
}