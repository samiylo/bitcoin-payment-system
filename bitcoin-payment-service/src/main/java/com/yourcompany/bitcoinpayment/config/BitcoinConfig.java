package com.yourcompany.bitcoinpayment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bitcoin")
public class BitcoinConfig {

    /**
     * "mainnet" or "testnet"
     */
    private String network = "testnet";
    private String xpub = "";
    private int requiredConfirmations = 1;
    private String mempoolBaseUrl = "https://mempool.space/testnet/api";

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getXpub() {
        return xpub;
    }

    public void setXpub(String xpub) {
        this.xpub = xpub;
    }

    public int getRequiredConfirmations() {
        return requiredConfirmations;
    }

    public void setRequiredConfirmations(int requiredConfirmations) {
        this.requiredConfirmations = requiredConfirmations;
    }

    public String getMempoolBaseUrl() {
        return mempoolBaseUrl;
    }

    public void setMempoolBaseUrl(String mempoolBaseUrl) {
        this.mempoolBaseUrl = mempoolBaseUrl;
    }
}
