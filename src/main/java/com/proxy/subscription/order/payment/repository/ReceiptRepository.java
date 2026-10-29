package com.proxy.subscription.order.payment.repository;

import com.proxy.subscription.order.payment.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByOrderIdOrderBySubmittedAtDesc(Long orderId);
}
