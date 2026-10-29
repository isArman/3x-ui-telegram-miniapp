package com.proxy.subscription.auth;

import com.proxy.subscription.auth.service.TelegramAuthServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramAuthServiceTest {

    private final TelegramAuthServiceImpl authService = new TelegramAuthServiceImpl();

    @Test
    void validateInitDataReturnsFalseForEmptyData() {
        assertThat(authService.validateInitData("", "token")).isFalse();
        assertThat(authService.validateInitData(null, "token")).isFalse();
    }

    @Test
    void extractUserIdReturnsNullForEmptyData() {
        assertThat(authService.extractUserId("")).isNull();
        assertThat(authService.extractUserId(null)).isNull();
    }
}
