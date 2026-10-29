package com.proxy.subscription.subscription.service;

import com.proxy.subscription.subscription.adapter.ThreeXUiSubscriptionAdapter;
import com.proxy.subscription.subscription.provider.SubscriptionProvider;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionProvisioningService {

    private final SubscriptionProvider provider;

    public SubscriptionProvisioningService(SubscriptionProvider provider) {
        this.provider = provider;
    }

    public String provisionSubscription(Long orderId, String email, String planName,
                                          Long dataGb, Integer durationDays,
                                          boolean isRenewal) {
        // Prevent duplicate subscription operations
        if (isRenewal) {
            String existingUrl = provider.getSubscriptionUrl(email);
            return provider.renewSubscription(existingUrl, planName, dataGb, durationDays);
        }
        return provider.createSubscription(email, planName, dataGb, durationDays);
    }

    public boolean retryProvisioning(Long orderId, String subscriptionUrl) {
        // Controlled retry initiated by administrator
        // Prevents duplicate operations by checking current state
        return provider.verifyCredentials(subscriptionUrl, null, null);
    }
}
