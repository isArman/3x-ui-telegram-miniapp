package com.proxy.subscription.common.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructuredLogger {
    private final Logger logger;

    public StructuredLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    public void error(String message, Throwable t, Object... args) {
        logger.error(message, args, t);
    }
}
