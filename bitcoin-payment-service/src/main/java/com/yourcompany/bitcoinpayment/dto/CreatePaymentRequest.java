package com.yourcompany.bitcoinpayment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreatePaymentRequest {

    @Size(max = 255)
    private String merchantReference;

    @NotBlank
    @Size(min = 3, max = 3)
    private String fiatCurrency;

    /**
     * Major units (e.g. USD dollars). Scale 2 is assumed for fiat in this API.
     */
    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal fiatAmount;

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
}
