package com.yourcompany.bitcoinpayment.event;

import com.yourcompany.bitcoinpayment.domain.Payment;

import java.util.UUID;

public class PaymentDetectedEvent {

    private final UUID paymentId;
    private final String txId;

    public PaymentDetectedEvent(Payment payment, String txId) {
        this.paymentId = payment.getId();
        this.txId = txId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public String getTxId() {
        return txId;
    }
}
