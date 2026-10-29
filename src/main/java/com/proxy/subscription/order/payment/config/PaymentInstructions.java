package com.proxy.subscription.order.payment.config;

import java.util.Map;

public interface PaymentInstructions {
    String getBankCardNumber();
    String getBankName();
    String getAccountHolderName();
    String getInstructionsText();
    void updateInstructions(String bankName, String cardNumber, String accountHolder, String instructions);
}
