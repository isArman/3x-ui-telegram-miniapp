package com.proxy.subscription.subscription.controller;

import com.proxy.subscription.auth.authorization.AdminAuthorization;
import com.proxy.subscription.subscription.service.SubscriptionProvisioningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/provisioning")
public class ProvisioningAdminController {

    private final SubscriptionProvisioningService provisioningService;
    private final AdminAuthorization adminAuthorization;

    public ProvisioningAdminController(SubscriptionProvisioningService provisioningService,
                                       AdminAuthorization adminAuthorization) {
        this.provisioningService = provisioningService;
        this.adminAuthorization = adminAuthorization;
    }

    @PostMapping("/retry")
    public ResponseEntity<String> retryProvisioning(
            @RequestHeader(value = "X-Telegram-User-Id", required = false) Long userId,
            @RequestBody Map<String, Object> payload) {
        adminAuthorization.enforceAdmin(userId);
        Long orderId = ((Number) payload.get("orderId")).longValue();
        String subscriptionUrl = (String) payload.get("subscriptionUrl");

        boolean success = provisioningService.retryProvisioning(orderId, subscriptionUrl);
        if (success) {
            return ResponseEntity.ok("Provisioning retry initiated for order " + orderId);
        } else {
            return ResponseEntity.status(500).body("Provisioning retry failed for order " + orderId);
        }
    }
}
