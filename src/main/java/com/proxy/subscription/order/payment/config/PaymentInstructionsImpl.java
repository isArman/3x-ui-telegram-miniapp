package com.proxy.subscription.order.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentInstructionsImpl implements PaymentInstructions {

    @Value("${payment.bank.name:}")
    private String bankName;

    @Value("${payment.bank.card.number:}")
    private String cardNumber;

    @Value("${payment.bank.account.holder:}")
    private String accountHolder;

    @Value("${payment.instructions.text:}")
    private String instructionsText;

    @Override
    public String getBankCardNumber() {
        return cardNumber;
    }

    @Override
    public String getBankName() {
        return bankName;
    }

    @Override
    public String getAccountHolderName() {
        return accountHolder;
    }

    @Override
    public String getInstructionsText() {
        return instructionsText != null ? instructionsText : "Please transfer to the card below and upload receipt.";
    }

    @Override
    public void updateInstructions(String bankName, String cardNumber, String accountHolder, String instructions) {
        this.bankName = bankName;
        this.cardNumber = cardNumber;
        this.accountHolder = accountHolder;
        this.instructionsText = instructions;
    }
}
