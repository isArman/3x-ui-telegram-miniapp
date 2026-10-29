package com.proxy.subscription.auth.authorization;

import com.proxy.subscription.auth.config.AdminAllowlist;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthorization {

    private final AdminAllowlist adminAllowlist;

    public AdminAuthorization(AdminAllowlist adminAllowlist) {
        this.adminAllowlist = adminAllowlist;
    }

    public boolean isAuthorized(Long telegramUserId) {
        return adminAllowlist.isAdmin(telegramUserId);
    }

    public void enforceAdmin(Long telegramUserId) {
        if (!isAuthorized(telegramUserId)) {
            throw new SecurityException("Access denied: user " + telegramUserId + " is not an administrator");
        }
    }
}
