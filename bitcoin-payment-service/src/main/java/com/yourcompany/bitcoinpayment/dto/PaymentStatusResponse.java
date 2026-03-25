package com.yourcompany.bitcoinpayment.dto;

import com.yourcompany.bitcoinpayment.domain.PaymentStatus;

import java.util.UUID;

public class PaymentStatusResponse {

    private UUID id;
    private PaymentStatus status;
    private String txId;
    private int confirmations;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }
}
