package com.vortexbird.paymentorders.history.entity;

import com.vortexbird.paymentorders.order.entity.PaymentOrder;
import com.vortexbird.paymentorders.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Registra cada cambio de estado realizado
 * sobre una orden de pago.
 */
@Entity
@Table(name = "order_status_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Orden afectada por el cambio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private PaymentOrder order;

    /**
     * Estado anterior.
     */
    @Column(name = "previous_status", nullable = false)
    private String previousStatus;

    /**
     * Nuevo estado.
     */
    @Column(name = "new_status", nullable = false)
    private String newStatus;

    /**
     * Fecha y hora del cambio.
     */
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    /**
     * Usuario que realizó la acción.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", nullable = false)
    private User changedBy;
}