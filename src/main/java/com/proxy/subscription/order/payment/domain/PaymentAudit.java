package com.proxy.subscription.order.payment.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "payment_audits")
public class PaymentAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "admin_telegram_user_id", nullable = false)
    private Long adminTelegramUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Column(length = 1000)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public enum AuditAction {
        RECEIPT_SUBMITTED, RECEIPT_APPROVED, RECEIPT_REJECTED,
        PAYMENT_CONFIRMED, PROVISIONING_ATTEMPTED, PROVISIONING_FAILED,
        PROVISIONING_SUCCEEDED, REPLACEMENT_RECEIPT_SUBMITTED
    }

    public PaymentAudit() {}

    public PaymentAudit(Long orderId, Long adminTelegramUserId, AuditAction action, String notes) {
        this.orderId = orderId;
        this.adminTelegramUserId = adminTelegramUserId;
        this.action = action;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getAdminTelegramUserId() { return adminTelegramUserId; }
    public void setAdminTelegramUserId(Long adminTelegramUserId) { this.adminTelegramUserId = adminTelegramUserId; }

    public AuditAction getAction() { return action; }
    public void setAction(AuditAction action) { this.action = action; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
