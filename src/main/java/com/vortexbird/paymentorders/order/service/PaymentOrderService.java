package com.vortexbird.paymentorders.order.service;

import com.vortexbird.paymentorders.exception.BusinessException;
import com.vortexbird.paymentorders.exception.ResourceNotFoundException;
import com.vortexbird.paymentorders.history.dto.OrderStatusHistoryResponse;
import com.vortexbird.paymentorders.history.repository.OrderStatusLogRepository;
import com.vortexbird.paymentorders.integration.dto.ApprovalNotificationRequest;
import com.vortexbird.paymentorders.integration.service.ExternalApprovalService;
import com.vortexbird.paymentorders.order.dto.CreateOrderRequest;
import com.vortexbird.paymentorders.order.dto.OrderResponse;
import com.vortexbird.paymentorders.order.entity.OrderStatus;
import com.vortexbird.paymentorders.order.entity.PaymentOrder;
import com.vortexbird.paymentorders.order.repository.PaymentOrderRepository;
import com.vortexbird.paymentorders.storage.service.FileStorageService;
import com.vortexbird.paymentorders.user.entity.User;
import com.vortexbird.paymentorders.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Gestiona la lógica de negocio relacionada
 * con las órdenes de pago.
 *
 * Responsable de:
 * - Crear órdenes
 * - Consultar órdenes
 * - Aprobar órdenes
 * - Rechazar órdenes
 */
@Service
@RequiredArgsConstructor
public class PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final UserRepository userRepository;
    private final ExternalApprovalService externalApprovalService;
    private final FileStorageService fileStorageService;
    private final OrderStatusLogRepository orderStatusLogRepository;

    /**
     * Crea una nueva orden de pago.
     */
    @Transactional
    public OrderResponse createOrder(
            CreateOrderRequest request,
            Authentication authentication
    ) {

        User creator = getCurrentUser(authentication);

        PaymentOrder order = PaymentOrder.builder()
                .description(request.description())
                .amount(request.amount())
                .createdBy(creator)
                .build();

        PaymentOrder savedOrder =
                paymentOrderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    /**
     * Obtiene todas las órdenes.
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return paymentOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Obtiene una orden por id.
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        PaymentOrder order = findOrder(id);

        return mapToResponse(order);
    }

    /**
     * Aprueba una orden pendiente.
     */
    @Transactional
    public OrderResponse approveOrder(
            Long id,
            Authentication authentication
    ) {

        PaymentOrder order = findOrder(id);

        validatePendingStatus(order);

        User approver = getCurrentUser(authentication);

        OrderStatus previousStatus = order.getStatus();

        order.setStatus(OrderStatus.APPROVED);
        order.setApprovedAt(LocalDateTime.now());
        order.setApprovedBy(approver);
        order.setLastModifiedBy(approver);

        externalApprovalService.notifyOrderApproved(
                new ApprovalNotificationRequest(
                        order.getId(),
                        order.getAmount(),
                        approver.getEmail(),
                        order.getApprovedAt()
                )
        );

        PaymentOrder savedOrder =
                paymentOrderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    /**
     * Rechaza una orden pendiente.
     */
    @Transactional
    public OrderResponse rejectOrder(
            Long id,
            Authentication authentication
    ) {

        PaymentOrder order = findOrder(id);

        validatePendingStatus(order);

        User rejector = getCurrentUser(authentication);

        OrderStatus previousStatus = order.getStatus();

        order.setStatus(OrderStatus.REJECTED);
        order.setRejectedAt(LocalDateTime.now());
        order.setRejectedBy(rejector);
        order.setLastModifiedBy(rejector);

        PaymentOrder savedOrder =
                paymentOrderRepository.save(order);

        return mapToResponse(savedOrder);
    }
    /**
     * Permite cargar archivos en las ordenes
     */
    @Transactional
    public String uploadInvoice(
            Long orderId,
            MultipartFile file
    ) {

        PaymentOrder order =
                findOrder(orderId);

        String filePath =
                fileStorageService.storeFile(
                        file,
                        orderId
                );

        order.setInvoicePath(filePath);

        paymentOrderRepository.save(order);

        return filePath;
    }

    /**
     * Obtiene la factura asociada a una orden.
     */
    @Transactional(readOnly = true)
    public Resource downloadInvoice(
            Long orderId
    ) {

        PaymentOrder order =
                findOrder(orderId);

        if (order.getInvoicePath() == null) {

            throw new ResourceNotFoundException(
                    "Invoice not found for order: "
                            + orderId
            );
        }

        try {

            Path path =
                    Paths.get(
                            order.getInvoicePath()
                    );

            Resource resource =
                    new UrlResource(
                            path.toUri()
                    );

            if (!resource.exists()) {

                throw new ResourceNotFoundException(
                        "Invoice file not found."
                );
            }

            return resource;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error loading invoice file",
                    e
            );
        }
    }

    /**
     * Permite acceder al historial de estados
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistoryResponse>
    getOrderHistory(Long orderId) {

        findOrder(orderId);

        return orderStatusLogRepository
                .findByOrderIdOrderByChangedAtDesc(
                        orderId
                )
                .stream()
                .map(log ->
                        new OrderStatusHistoryResponse(
                                log.getPreviousStatus(),
                                log.getNewStatus(),
                                log.getChangedAt(),
                                log.getChangedBy()
                                        .getEmail()
                        )
                )
                .toList();
    }

    private PaymentOrder findOrder(Long id) {

        return paymentOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + id
                        ));
    }

    private User getCurrentUser(
            Authentication authentication
    ) {

        return userRepository.findByEmail(
                        authentication.getName()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated user not found"
                        ));
    }

    private void validatePendingStatus(
            PaymentOrder order
    ) {

        if (order.getStatus() != OrderStatus.PENDING) {

            throw new BusinessException(
                    "Only pending orders can be processed."
            );
        }
    }

    private OrderResponse mapToResponse(
            PaymentOrder order
    ) {

        return new OrderResponse(
                order.getId(),
                order.getDescription(),
                order.getAmount(),
                order.getStatus().name(),

                order.getCreatedBy() != null
                        ? order.getCreatedBy().getEmail()
                        : null,

                order.getApprovedBy() != null
                        ? order.getApprovedBy().getEmail()
                        : null,

                order.getRejectedBy() != null
                        ? order.getRejectedBy().getEmail()
                        : null,

                order.getInvoicePath(),

                order.getCreatedAt(),
                order.getApprovedAt(),
                order.getRejectedAt()
        );
    }
}