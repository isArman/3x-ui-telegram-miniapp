package com.proxy.subscription.subscription.service;

import com.proxy.subscription.subscription.provider.SubscriptionProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SubscriptionProvisioningServiceTest {

    @Test
    void provisionSubscriptionCreatesSubscription() {
        SubscriptionProvider mockProvider = mock(SubscriptionProvider.class);
        when(mockProvider.createSubscription(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn("http://example.com/sub/test");

        SubscriptionProvisioningService service = new SubscriptionProvisioningService(mockProvider);
        String result = service.provisionSubscription(1L, "test@example.com", "Basic", 10L, 30, false);

        assertThat(result).isEqualTo("http://example.com/sub/test");
        verify(mockProvider).createSubscription("test@example.com", "Basic", 10L, 30);
    }

    @Test
    void provisionSubscriptionRenewsSubscription() {
        SubscriptionProvider mockProvider = mock(SubscriptionProvider.class);
        when(mockProvider.getSubscriptionUrl(anyString())).thenReturn("http://example.com/sub/test");
        when(mockProvider.renewSubscription(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn("http://example.com/sub/test");

        SubscriptionProvisioningService service = new SubscriptionProvisioningService(mockProvider);
        String result = service.provisionSubscription(1L, "test@example.com", "Basic", 20L, 30, true);

        assertThat(result).isEqualTo("http://example.com/sub/test");
        verify(mockProvider).getSubscriptionUrl("test@example.com");
        verify(mockProvider).renewSubscription("http://example.com/sub/test", "Basic", 20L, 30);
    }
}
