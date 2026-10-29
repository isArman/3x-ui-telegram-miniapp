package com.proxy.subscription.subscription;

import com.proxy.subscription.subscription.provider.SubscriptionProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionProviderTest {

    @Test
    void providerInterfaceExists() {
        assertThat(SubscriptionProvider.class).isNotNull();
    }
}
