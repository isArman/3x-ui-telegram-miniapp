package com.proxy.subscription.auth.service;

public interface TelegramAuthService {
    boolean validateInitData(String initData, String botToken);
    Long extractUserId(String initData);
}
