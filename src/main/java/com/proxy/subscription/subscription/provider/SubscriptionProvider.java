package com.proxy.subscription.subscription.provider;

public interface SubscriptionProvider {

    String createSubscription(String email, String planName, Long dataGb, Integer durationDays);

    String renewSubscription(String subscriptionUrl, String planName, Long dataGb, Integer durationDays);

    String getSubscriptionUrl(String email);

    boolean verifyCredentials(String url, String username, String password);
}
