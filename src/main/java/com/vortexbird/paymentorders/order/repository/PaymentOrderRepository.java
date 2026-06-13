package com.vortexbird.paymentorders.order.repository;

import com.vortexbird.paymentorders.order.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    List<PaymentOrder> findByArchivedFalse();

    List<PaymentOrder> findByArchivedTrue();
}