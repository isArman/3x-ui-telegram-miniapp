package com.proxy.subscription.order.controller;

import com.proxy.subscription.auth.service.TelegramAuthService;
import com.proxy.subscription.auth.service.TelegramAuthServiceImpl;
import com.proxy.subscription.auth.authorization.AdminAuthorization;
import com.proxy.subscription.auth.config.AdminAllowlistImpl;
import com.proxy.subscription.order.domain.Order;
import com.proxy.subscription.order.repository.OrderRepository;
import com.proxy.subscription.plan.domain.Plan;
import com.proxy.subscription.plan.repository.PlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private PlanRepository planRepository;

    @MockBean
    private TelegramAuthService telegramAuthService;

    @MockBean
    private AdminAuthorization adminAuthorization;

    @Test
    void createOrderRequiresValidInitData() throws Exception {
        when(telegramAuthService.validateInitData(anyString(), anyString())).thenReturn(true);
        when(telegramAuthService.extractUserId(anyString())).thenReturn(12345L);
        when(planRepository.findById(anyLong())).thenReturn(Optional.of(
                new Plan("Basic", 10L, 30, new BigDecimal("50000"))
        ));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(post("/orders")
                        .header("X-Telegram-Init-Data", "test")
                        .header("X-Telegram-Bot-Token", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"planId\":1}"))
                .andExpect(status().isCreated());
    }
}
