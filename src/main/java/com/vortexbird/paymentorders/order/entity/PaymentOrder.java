package com.vortexbird.paymentorders.order.entity;

import com.vortexbird.paymentorders.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa una orden de pago dentro del sistema.
 *
 * Una orden puede encontrarse en estado:
 * PENDING, APPROVED o REJECTED.
 */
@Entity
@Table(name = "payment_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descripción de la orden.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Valor monetario de la orden.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * Estado actual de la orden.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /**
     * Ruta donde se almacena la factura.
     */
    private String invoicePath;

    /**
     * Fecha de creación.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha de aprobación.
     */
    private LocalDateTime approvedAt;

    /**
     * Usuario que creó la orden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    /**
     * Usuario que aprobó la orden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    /**
     * Usuario que rechazó la orden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejected_by")
    private User rejectedBy;

    /**
     * Marca utilizada por el Stored Procedure
     * para archivar órdenes rechazadas.
     */
    @Column(nullable = false)
    private Boolean archived;
}