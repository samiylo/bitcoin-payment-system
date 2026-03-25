package com.yourcompany.bitcoinpayment.event;

import com.yourcompany.bitcoinpayment.domain.Payment;

import java.util.UUID;

public class PaymentConfirmedEvent {

    private final UUID paymentId;
    private final String txId;
    private final int confirmations;

    public PaymentConfirmedEvent(Payment payment) {
        this.paymentId = payment.getId();
        this.txId = payment.getTxId();
        this.confirmations = payment.getConfirmations();
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public String getTxId() {
        return txId;
    }

    public int getConfirmations() {
        return confirmations;
    }
}
