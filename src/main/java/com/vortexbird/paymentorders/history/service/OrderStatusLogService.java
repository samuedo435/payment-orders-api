package com.vortexbird.paymentorders.history.service;

import com.vortexbird.paymentorders.history.entity.OrderStatusLog;
import com.vortexbird.paymentorders.history.repository.OrderStatusLogRepository;
import com.vortexbird.paymentorders.order.entity.OrderStatus;
import com.vortexbird.paymentorders.order.entity.PaymentOrder;
import com.vortexbird.paymentorders.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Gestiona el registro histórico de cambios
 * de estado de las órdenes.
 */
@Service
@RequiredArgsConstructor
public class OrderStatusLogService {

    private final OrderStatusLogRepository repository;

    public void registerStatusChange(
            PaymentOrder order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            User changedBy
    ) {

        OrderStatusLog log = OrderStatusLog.builder()
                .order(order)
                .previousStatus(previousStatus.name())
                .newStatus(newStatus.name())
                .changedAt(LocalDateTime.now())
                .changedBy(changedBy)
                .build();

        repository.save(log);
    }
}