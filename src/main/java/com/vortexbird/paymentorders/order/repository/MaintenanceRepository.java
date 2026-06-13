package com.vortexbird.paymentorders.order.repository;

import com.vortexbird.paymentorders.order.dto.ArchiveOrdersResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MaintenanceRepository {

    ArchiveOrdersResponse archiveRejectedOrders(
            LocalDateTime cutoffDate
    );
}