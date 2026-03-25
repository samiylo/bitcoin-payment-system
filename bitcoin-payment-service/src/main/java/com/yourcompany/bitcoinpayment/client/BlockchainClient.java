package com.yourcompany.bitcoinpayment.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.yourcompany.bitcoinpayment.config.BitcoinConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlockchainClient {

    private final WebClient webClient;

    public BlockchainClient(WebClient.Builder webClientBuilder, BitcoinConfig bitcoinConfig) {
        String base = bitcoinConfig.getMempoolBaseUrl().replaceAll("/+$", "");
        this.webClient = webClientBuilder.clone().baseUrl(base).build();
    }

    public List<JsonNode> listAddressTransactions(String address) {
        try {
            JsonNode arr = webClient
                    .get()
                    .uri("/address/{address}/txs", address)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            if (arr == null || !arr.isArray()) {
                return List.of();
            }
            List<JsonNode> out = new ArrayList<>();
            arr.forEach(out::add);
            return out;
        } catch (WebClientResponseException.NotFound e) {
            return List.of();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Blockchain API error: " + e.getStatusCode(), e);
        }
    }

    public JsonNode getTransaction(String txid) {
        try {
            return webClient
                    .get()
                    .uri("/tx/{txid}", txid)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Blockchain API error: " + e.getStatusCode(), e);
        }
    }

    /**
     * Chain tip height from mempool.space-style API (plain-text body).
     */
    public int getTipBlockHeight() {
        try {
            String body = webClient
                    .get()
                    .uri("/blocks/tip/height")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if (body == null || body.isBlank()) {
                return 0;
            }
            return Integer.parseInt(body.trim());
        } catch (WebClientResponseException e) {
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
