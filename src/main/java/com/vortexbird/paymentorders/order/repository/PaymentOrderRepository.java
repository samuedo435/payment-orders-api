package com.vortexbird.paymentorders.order.repository;

import com.vortexbird.paymentorders.order.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
}