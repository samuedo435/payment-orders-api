package com.vortexbird.paymentorders.order.controller;

import com.vortexbird.paymentorders.order.dto.CreateOrderRequest;
import com.vortexbird.paymentorders.order.dto.OrderResponse;
import com.vortexbird.paymentorders.order.service.PaymentOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.vortexbird.paymentorders.storage.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Expone los endpoints relacionados con
 * la gestión de órdenes de pago.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class PaymentOrderController {

    private final PaymentOrderService paymentOrderService;

    @PostMapping
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            Authentication authentication
    ) {

        return paymentOrderService.createOrder(
                request,
                authentication
        );
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {

        return paymentOrderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getById(
            @PathVariable Long id
    ) {

        return paymentOrderService.getOrderById(id);
    }

    @PutMapping("/{id}/approve")
    public OrderResponse approveOrder(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return paymentOrderService.approveOrder(
                id,
                authentication
        );
    }

    @PutMapping("/{id}/reject")
    public OrderResponse rejectOrder(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return paymentOrderService.rejectOrder(
                id,
                authentication
        );
    }

    @PostMapping("/{id}/invoice")
    public FileUploadResponse uploadInvoice(
            @PathVariable Long id,
            @RequestParam("file")
            MultipartFile file
    ) {

        String filePath =
                paymentOrderService.uploadInvoice(
                        id,
                        file
                );

        return new FileUploadResponse(
                "Invoice uploaded successfully",
                filePath
        );
    }
}