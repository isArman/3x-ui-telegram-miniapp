package com.proxy.subscription.subscription.controller;

import com.proxy.subscription.auth.authorization.AdminAuthorization;
import com.proxy.subscription.auth.config.AdminAllowlistImpl;
import com.proxy.subscription.subscription.service.SubscriptionProvisioningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProvisioningAdminController.class)
class ProvisioningAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionProvisioningService provisioningService;

    @MockBean
    private AdminAuthorization adminAuthorization;

    @Test
    void retryProvisioningRequiresAdmin() throws Exception {
        when(adminAuthorization.isAuthorized(anyLong())).thenReturn(true);
        when(provisioningService.retryProvisioning(anyLong(), anyString())).thenReturn(true);

        mockMvc.perform(post("/admin/provisioning/retry")
                        .header("X-Telegram-User-Id", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":1,\"subscriptionUrl\":\"http://example.com/sub/1\"}"))
                .andExpect(status().isOk());
    }
}
