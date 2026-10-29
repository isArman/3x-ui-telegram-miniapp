package com.proxy.subscription.order.payment.controller;

import com.proxy.subscription.auth.authorization.AdminAuthorization;
import com.proxy.subscription.order.domain.Order;
import com.proxy.subscription.order.payment.domain.PaymentAudit;
import com.proxy.subscription.order.payment.repository.PaymentAuditRepository;
import com.proxy.subscription.order.payment.service.ReceiptService;
import com.proxy.subscription.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/payments")
public class PaymentAdminController {

    private final OrderRepository orderRepository;
    private final PaymentAuditRepository auditRepository;
    private final AdminAuthorization adminAuthorization;
    private final ReceiptService receiptService;

    public PaymentAdminController(OrderRepository orderRepository,
                                   PaymentAuditRepository auditRepository,
                                   AdminAuthorization adminAuthorization,
                                   ReceiptService receiptService) {
        this.orderRepository = orderRepository;
        this.auditRepository = auditRepository;
        this.adminAuthorization = adminAuthorization;
        this.receiptService = receiptService;
    }

    @PostMapping("/approve")
    public ResponseEntity<Order> approvePayment(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId,
            @RequestBody Map<String, Object> payload) {
        adminAuthorization.enforceAdmin(userId);
        Long orderId = ((Number) payload.get("orderId")).longValue();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Prevent duplicate approval
        if (order.getStatus() == Order.OrderStatus.PAYMENT_APPROVED ||
            order.getStatus() == Order.OrderStatus.PROVISIONED ||
            order.getStatus() == Order.OrderStatus.PROVISIONING_PENDING) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(order);
        }

        order.setStatus(Order.OrderStatus.PAYMENT_APPROVED);
        orderRepository.save(order);

        auditRepository.save(new PaymentAudit(
                orderId,
                userId,
                PaymentAudit.AuditAction.PAYMENT_CONFIRMED,
                "Payment approved by administrator"
        ));

        return ResponseEntity.ok(order);
    }

    @PostMapping("/reject")
    public ResponseEntity<Order> rejectPayment(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId,
            @RequestBody Map<String, Object> payload) {
        adminAuthorization.enforceAdmin(userId);
        Long orderId = ((Number) payload.get("orderId")).longValue();
        String reason = (String) payload.getOrDefault("reason", "No reason provided");

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(Order.OrderStatus.REJECTED);
        orderRepository.save(order);

        auditRepository.save(new PaymentAudit(
                orderId,
                userId,
                PaymentAudit.AuditAction.RECEIPT_REJECTED,
                reason
        ));

        return ResponseEntity.ok(order);
    }

    @GetMapping("/audit/{orderId}")
    public ResponseEntity<java.util.List<PaymentAudit>> getAuditTrail(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId,
            @PathVariable Long orderId) {
        adminAuthorization.enforceAdmin(userId);
        return ResponseEntity.ok(auditRepository.findByOrderIdOrderByCreatedAtAsc(orderId));
    }
}
