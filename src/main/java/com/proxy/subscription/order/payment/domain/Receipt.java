package com.proxy.subscription.order.payment.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt = Instant.now();

    @Column(name = "replaced_by_receipt_id")
    private Long replacedByReceiptId;

    public Receipt() {}

    public Receipt(Long orderId, String filePath, String originalFilename) {
        this.orderId = orderId;
        this.filePath = filePath;
        this.originalFilename = originalFilename;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }

    public Long getReplacedByReceiptId() { return replacedByReceiptId; }
    public void setReplacedByReceiptId(Long replacedByReceiptId) { this.replacedByReceiptId = replacedByReceiptId; }
}
