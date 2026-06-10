package com.vortexbird.paymentorders.order.service;

import com.vortexbird.paymentorders.exception.BusinessException;
import com.vortexbird.paymentorders.exception.ResourceNotFoundException;
import com.vortexbird.paymentorders.order.dto.CreateOrderRequest;
import com.vortexbird.paymentorders.order.dto.OrderResponse;
import com.vortexbird.paymentorders.order.entity.OrderStatus;
import com.vortexbird.paymentorders.order.entity.PaymentOrder;
import com.vortexbird.paymentorders.order.repository.PaymentOrderRepository;
import com.vortexbird.paymentorders.user.entity.User;
import com.vortexbird.paymentorders.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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

    /**
     * Crea una nueva orden de pago.
     */
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
    public List<OrderResponse> getAllOrders() {

        return paymentOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Obtiene una orden por id.
     */
    public OrderResponse getOrderById(Long id) {

        PaymentOrder order = findOrder(id);

        return mapToResponse(order);
    }

    /**
     * Aprueba una orden pendiente.
     */
    public OrderResponse approveOrder(
            Long id,
            Authentication authentication
    ) {

        PaymentOrder order = findOrder(id);

        validatePendingStatus(order);

        User approver = getCurrentUser(authentication);

        order.setStatus(OrderStatus.APPROVED);
        order.setApprovedAt(LocalDateTime.now());
        order.setApprovedBy(approver);

        PaymentOrder savedOrder =
                paymentOrderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    /**
     * Rechaza una orden pendiente.
     */
    public OrderResponse rejectOrder(
            Long id,
            Authentication authentication
    ) {

        PaymentOrder order = findOrder(id);

        validatePendingStatus(order);

        User rejector = getCurrentUser(authentication);

        order.setStatus(OrderStatus.REJECTED);
        order.setRejectedAt(LocalDateTime.now());
        order.setRejectedBy(rejector);

        PaymentOrder savedOrder =
                paymentOrderRepository.save(order);

        return mapToResponse(savedOrder);
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