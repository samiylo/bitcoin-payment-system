package com.yourcompany.bitcoinpayment.service;

import com.yourcompany.bitcoinpayment.client.PriceClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    private final PriceClient priceClient;

    public PricingService(PriceClient priceClient) {
        this.priceClient = priceClient;
    }

    public BigDecimal bitcoinPriceInFiat(String fiatCurrency) {
        return priceClient.bitcoinPriceInFiat(fiatCurrency);
    }
}
