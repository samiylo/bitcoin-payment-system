package com.yourcompany.bitcoinpayment.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("payments")
public class Payment {

    @Id
    private UUID id;
    @Column("merchant_reference")
    private String merchantReference;
    @Column("fiat_currency")
    private String fiatCurrency;
    @Column("fiat_amount_minor")
    private long fiatAmountMinor;
    @Column("amount_satoshi")
    private long amountSatoshi;
    @Column("btc_address")
    private String btcAddress;
    private PaymentStatus status;
    @Column("tx_id")
    private String txId;
    private int confirmations;
    @Column("created_at")
    private Instant createdAt;
    @Column("updated_at")
    private Instant updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public long getFiatAmountMinor() {
        return fiatAmountMinor;
    }

    public void setFiatAmountMinor(long fiatAmountMinor) {
        this.fiatAmountMinor = fiatAmountMinor;
    }

    public long getAmountSatoshi() {
        return amountSatoshi;
    }

    public void setAmountSatoshi(long amountSatoshi) {
        this.amountSatoshi = amountSatoshi;
    }

    public String getBtcAddress() {
        return btcAddress;
    }

    public void setBtcAddress(String btcAddress) {
        this.btcAddress = btcAddress;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
