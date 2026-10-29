package com.proxy.subscription.auth.authorization;

import com.proxy.subscription.auth.config.AdminAllowlist;
import com.proxy.subscription.auth.config.AdminAllowlistImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdminAuthorizationTest {

    @Test
    void enforceAdminThrowsForNonAdmin() {
        AdminAllowlist allowlist = new AdminAllowlistImpl();
        AdminAuthorization auth = new AdminAuthorization(allowlist);
        try {
            auth.enforceAdmin(999L);
            assertThat(false).isTrue(); // Should have thrown
        } catch (SecurityException e) {
            assertThat(e.getMessage()).contains("Access denied");
        }
    }
}
