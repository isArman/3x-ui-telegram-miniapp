package com.proxy.subscription.order.payment.controller;

import com.proxy.subscription.auth.authorization.AdminAuthorization;
import com.proxy.subscription.auth.config.AdminAllowlistImpl;
import com.proxy.subscription.order.domain.Order;
import com.proxy.subscription.order.payment.repository.PaymentAuditRepository;
import com.proxy.subscription.order.repository.OrderRepository;
import com.proxy.subscription.order.payment.service.ReceiptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentAdminController.class)
class PaymentAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private PaymentAuditRepository auditRepository;

    @MockBean
    private AdminAuthorization adminAuthorization;

    @MockBean
    private ReceiptService receiptService;

    @Test
    void approvePaymentRequiresAdmin() throws Exception {
        when(adminAuthorization.isAuthorized(anyLong())).thenReturn(true);

        Order order = new Order(123456L, 1L, "Basic", 10L, 30, new BigDecimal("50000"));
        order.setId(1L);
        order.setStatus(Order.OrderStatus.PENDING_PAYMENT);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(post("/admin/payments/approve")
                        .header("X-Telegram-User-Id", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":1}"))
                .andExpect(status().isOk());
    }
}
