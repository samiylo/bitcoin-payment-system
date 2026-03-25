package com.yourcompany.bitcoinpayment.dto;

import com.yourcompany.bitcoinpayment.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class PaymentResponse {

    private UUID id;
    private String merchantReference;
    private String fiatCurrency;
    private BigDecimal fiatAmount;
    private long amountSatoshi;
    private String btcAddress;
    private PaymentStatus status;
    private String paymentUri;
    private String qrCodePngBase64;
    private Instant createdAt;

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

    public BigDecimal getFiatAmount() {
        return fiatAmount;
    }

    public void setFiatAmount(BigDecimal fiatAmount) {
        this.fiatAmount = fiatAmount;
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

    public String getPaymentUri() {
        return paymentUri;
    }

    public void setPaymentUri(String paymentUri) {
        this.paymentUri = paymentUri;
    }

    public String getQrCodePngBase64() {
        return qrCodePngBase64;
    }

    public void setQrCodePngBase64(String qrCodePngBase64) {
        this.qrCodePngBase64 = qrCodePngBase64;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
