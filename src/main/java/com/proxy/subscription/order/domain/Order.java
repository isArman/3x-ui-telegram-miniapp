package com.proxy.subscription.order.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_user_id", nullable = false)
    private Long telegramUserId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "plan_snapshot_name", nullable = false)
    private String planSnapshotName;

    @Column(name = "plan_snapshot_data_gb", nullable = false)
    private Long planSnapshotDataGb;

    @Column(name = "plan_snapshot_duration_days", nullable = false)
    private Integer planSnapshotDurationDays;

    @Column(name = "plan_snapshot_price_toman", nullable = false, precision = 15, scale = 2)
    private BigDecimal planSnapshotPriceToman;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public enum OrderStatus {
        CREATED, PENDING_PAYMENT, PAYMENT_APPROVED, PROVISIONING_PENDING,
        PROVISIONED, PROVISIONING_FAILED, REJECTED
    }

    public Order() {}

    public Order(Long telegramUserId, Long planId, String planSnapshotName,
                 Long planSnapshotDataGb, Integer planSnapshotDurationDays,
                 BigDecimal planSnapshotPriceToman) {
        this.telegramUserId = telegramUserId;
        this.planId = planId;
        this.planSnapshotName = planSnapshotName;
        this.planSnapshotDataGb = planSnapshotDataGb;
        this.planSnapshotDurationDays = planSnapshotDurationDays;
        this.planSnapshotPriceToman = planSnapshotPriceToman;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTelegramUserId() { return telegramUserId; }
    public void setTelegramUserId(Long telegramUserId) { this.telegramUserId = telegramUserId; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public String getPlanSnapshotName() { return planSnapshotName; }
    public void setPlanSnapshotName(String planSnapshotName) { this.planSnapshotName = planSnapshotName; }

    public Long getPlanSnapshotDataGb() { return planSnapshotDataGb; }
    public void setPlanSnapshotDataGb(Long planSnapshotDataGb) { this.planSnapshotDataGb = planSnapshotDataGb; }

    public Integer getPlanSnapshotDurationDays() { return planSnapshotDurationDays; }
    public void setPlanSnapshotDurationDays(Integer planSnapshotDurationDays) { this.planSnapshotDurationDays = planSnapshotDurationDays; }

    public BigDecimal getPlanSnapshotPriceToman() { return planSnapshotPriceToman; }
    public void setPlanSnapshotPriceToman(BigDecimal planSnapshotPriceToman) { this.planSnapshotPriceToman = planSnapshotPriceToman; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
