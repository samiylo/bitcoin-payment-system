package com.yourcompany.bitcoinpayment.client;

import com.yourcompany.bitcoinpayment.config.PricingConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class PriceClient {

    private final WebClient webClient;

    public PriceClient(WebClient.Builder webClientBuilder, PricingConfig pricingConfig) {
        this.webClient =
                webClientBuilder.clone().baseUrl(pricingConfig.getBaseUrl()).build();
    }

    /**
     * Spot price: units of {@code fiatCurrency} per 1 BTC (major fiat units, e.g. USD).
     */
    public BigDecimal bitcoinPriceInFiat(String fiatCurrency) {
        String vs = fiatCurrency.toLowerCase();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/simple/price")
                            .queryParam("ids", "bitcoin")
                            .queryParam("vs_currencies", vs)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (body == null) {
                throw new IllegalStateException("Empty price response");
            }
            @SuppressWarnings("unchecked")
            Map<String, Number> bitcoin = (Map<String, Number>) body.get("bitcoin");
            if (bitcoin == null) {
                throw new IllegalStateException("Missing bitcoin key in price response");
            }
            Number n = bitcoin.get(vs);
            if (n == null) {
                throw new IllegalStateException("Missing vs_currency in price response: " + vs);
            }
            return BigDecimal.valueOf(n.doubleValue());
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Price API error: " + e.getStatusCode(), e);
        }
    }
}
