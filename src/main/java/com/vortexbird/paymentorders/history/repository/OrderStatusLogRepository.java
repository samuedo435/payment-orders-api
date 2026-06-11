package com.vortexbird.paymentorders.history.repository;

import com.vortexbird.paymentorders.history.entity.OrderStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusLogRepository
        extends JpaRepository<OrderStatusLog, Long> {
}