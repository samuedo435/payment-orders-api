package com.vortexbird.paymentorders.order.controller;

import com.vortexbird.paymentorders.order.dto.CreateOrderRequest;
import com.vortexbird.paymentorders.order.dto.OrderResponse;
import com.vortexbird.paymentorders.order.service.PaymentOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.vortexbird.paymentorders.storage.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Expone los endpoints relacionados con
 * la gestión de órdenes de pago.
 */
@Tag(
        name = "Payment Orders",
        description = "Endpoints for managing payment orders."
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class PaymentOrderController {

    private final PaymentOrderService paymentOrderService;

    @Operation(
            summary = "Create payment order",
            description = "Creates a new payment order with PENDING status."
    )
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

    @Operation(
            summary = "Get all payment orders",
            description = "Returns the list of payment orders available to the authenticated user."
    )
    @GetMapping
    public List<OrderResponse> getAllOrders() {

        return paymentOrderService.getAllOrders();
    }

    @Operation(
            summary = "Get payment order by ID",
            description = "Returns detailed information of a specific payment order."
    )
    @GetMapping("/{id}")
    public OrderResponse getById(
            @PathVariable Long id
    ) {

        return paymentOrderService.getOrderById(id);
    }

    @Operation(
            summary = "Approve payment order",
            description = "Approves a payment order. Only users with ADMIN role can perform this action."
    )
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

    @Operation(
            summary = "Reject payment order",
            description = "Rejects a payment order. Only users with ADMIN role can perform this action."
    )
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

    @Operation(
            summary = "Upload invoice",
            description = "Uploads and associates an invoice file with a payment order."
    )
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

    @Operation(
            summary = "Download invoice",
            description = "Downloads the invoice associated with a payment order."
    )
    @GetMapping("/{id}/invoice")
    public ResponseEntity<Resource> downloadInvoice(
            @PathVariable Long id
    ) {

        Resource resource =
                paymentOrderService.downloadInvoice(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + resource.getFilename()
                                + "\""
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(resource);
    }
}