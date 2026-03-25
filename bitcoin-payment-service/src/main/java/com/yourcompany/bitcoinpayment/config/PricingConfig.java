package com.yourcompany.bitcoinpayment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pricing.coingecko")
public class PricingConfig {

    private String baseUrl = "https://api.coingecko.com/api/v3";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
