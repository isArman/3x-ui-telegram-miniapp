package com.proxy.subscription.subscription.adapter;

import com.proxy.subscription.subscription.provider.SubscriptionProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ThreeXUiSubscriptionAdapterTest {

    @Test
    void adapterImplementsProvider() {
        assertThat(SubscriptionProvider.class).isAssignableFrom(ThreeXUiSubscriptionAdapter.class);
    }

    @Test
    void verifyCredentialsReturnsTrueForValidInputs() {
        ThreeXUiSubscriptionAdapter adapter = new ThreeXUiSubscriptionAdapter();
        assertThat(adapter.verifyCredentials("http://example.com", "token", "token")).isTrue();
    }

    @Test
    void verifyCredentialsReturnsFalseForEmptyUrl() {
        ThreeXUiSubscriptionAdapter adapter = new ThreeXUiSubscriptionAdapter();
        assertThat(adapter.verifyCredentials("", "token", "token")).isFalse();
    }
}
