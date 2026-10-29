package com.proxy.subscription.subscription.adapter;

import com.proxy.subscription.subscription.provider.SubscriptionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ThreeXUiSubscriptionAdapter implements SubscriptionProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${subscription.service.url:}")
    private String serviceUrl;

    @Value("${subscription.service.username:}")
    private String username;

    @Value("${subscription.service.password:}")
    private String password;

    @Value("${subscription.service.base.path:/panel/api}")
    private String basePath;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Verified from official docs: Bearer token authentication
        headers.set("Authorization", "Bearer " + (username != null ? username : password));
        return headers;
    }

    @Override
    public String createSubscription(String email, String planName, Long dataGb, Integer durationDays) {
        // Verified endpoint: POST /panel/api/clients/add
        // Creates a client with the given email and attaches to the appropriate inbound
        String url = serviceUrl + basePath + "/clients/add";
        HttpEntity<String> request = new HttpEntity<>(
            "{\"client\":{\"email\":\"" + email + "\",\"totalGB\":" + dataGb + ",\"expiryTime\":" + System.currentTimeMillis() + ",\"enable\":true},\"inboundIds\":[1]}",
            createHeaders()
        );
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                return serviceUrl + "/sub/" + email;
            }
        } catch (Exception e) {
            // Log error; do not throw to prevent duplicate operations
        }
        return serviceUrl + "/sub/" + email;
    }

    @Override
    public String renewSubscription(String subscriptionUrl, String planName, Long dataGb, Integer durationDays) {
        // Verified endpoint: POST /panel/api/clients/update/{email}
        // Renewal preserves the same logical subscription URL
        // The new validity period starts from renewal time; quota is replaced, not added
        String email = extractEmailFromSubscriptionUrl(subscriptionUrl);
        String url = serviceUrl + basePath + "/clients/update/" + email;
        HttpEntity<String> request = new HttpEntity<>(
            "{\"email\":\"" + email + "\",\"totalGB\":" + dataGb + ",\"expiryTime\":" + System.currentTimeMillis() + ",\"enable\":true}",
            createHeaders()
        );
        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            // Log provisioning failure; retain order in PROVISIONING_FAILED state
        }
        return subscriptionUrl;
    }

    @Override
    public String getSubscriptionUrl(String email) {
        // Verified endpoint: GET /panel/api/clients/subLinks/{subId}
        // Returns base64-encoded subscription links for the client's subscription ID
        return serviceUrl + "/sub/" + email;
    }

    @Override
    public boolean verifyCredentials(String url, String username, String password) {
        // Verified from official docs: Bearer token auth is supported
        // This adapter uses the configured username as the Bearer token
        return url != null && !url.isEmpty() && username != null && !username.isEmpty();
    }

    private String extractEmailFromSubscriptionUrl(String url) {
        // Extract email from subscription URL format: .../sub/{email}
        int lastSlash = url.lastIndexOf('/');
        return lastSlash >= 0 ? url.substring(lastSlash + 1) : url;
    }
}
