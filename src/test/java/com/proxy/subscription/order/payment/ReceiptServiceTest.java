package com.proxy.subscription.order.payment;

import com.proxy.subscription.order.payment.service.ReceiptService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptServiceTest {

    @Test
    void receiptServiceExists() {
        assertThat(ReceiptService.class).isNotNull();
    }
}
