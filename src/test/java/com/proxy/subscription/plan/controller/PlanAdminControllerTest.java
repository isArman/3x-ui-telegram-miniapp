package com.proxy.subscription.plan.controller;

import com.proxy.subscription.auth.authorization.AdminAuthorization;
import com.proxy.subscription.auth.config.AdminAllowlistImpl;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanAdminController.class)
class PlanAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanRepository planRepository;

    @MockBean
    private AdminAuthorization adminAuthorization;

    @Test
    void listPlansRequiresAdmin() throws Exception {
        when(adminAuthorization.isAuthorized(any())).thenReturn(true);
        when(planRepository.findAll()).thenReturn(Collections.singletonList(
                new Plan("Basic", 10L, 30, new BigDecimal("50000"))
        ));

        mockMvc.perform(get("/admin/plans")
                        .header("X-Telegram-User-Id", "123456"))
                .andExpect(status().isOk());
    }
}
