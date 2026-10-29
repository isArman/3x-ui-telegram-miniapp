package com.proxy.subscription.auth.config;

import java.util.Set;

public interface AdminAllowlist {
    boolean isAdmin(Long telegramUserId);
    Set<Long> getAllowedIds();
}
