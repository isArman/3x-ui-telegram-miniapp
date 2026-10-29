package com.proxy.subscription.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class AdminAllowlistImpl implements AdminAllowlist {

    @Value("${admin.telegram.ids:}")
    private String adminTelegramIdsRaw;

    @Override
    public boolean isAdmin(Long telegramUserId) {
        if (telegramUserId == null) {
            return false;
        }
        return getAllowedIds().contains(telegramUserId);
    }

    @Override
    public Set<Long> getAllowedIds() {
        if (adminTelegramIdsRaw == null || adminTelegramIdsRaw.trim().isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> ids = new HashSet<>();
        String[] parts = adminTelegramIdsRaw.split(",");
        for (String part : parts) {
            try {
                ids.add(Long.parseLong(part.trim()));
            } catch (NumberFormatException e) {
                // Skip invalid IDs
            }
        }
        return ids;
    }
}
