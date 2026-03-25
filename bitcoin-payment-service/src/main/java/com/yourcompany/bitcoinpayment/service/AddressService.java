package com.yourcompany.bitcoinpayment.service;

import com.yourcompany.bitcoinpayment.config.BitcoinConfig;
import com.yourcompany.bitcoinpayment.repository.AddressIndexRepository;
import org.bitcoinj.base.SegwitAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Derives native SegWit (P2WPKH) receive addresses from an account-level extended public key
 * (BIP84 {@code zpub}/{@code vpub} or equivalent), then {@code /0/index}.
 */
@Service
public class AddressService {

    private final BitcoinConfig bitcoinConfig;
    private final AddressIndexRepository addressIndexRepository;

    public AddressService(BitcoinConfig bitcoinConfig, AddressIndexRepository addressIndexRepository) {
        this.bitcoinConfig = bitcoinConfig;
        this.addressIndexRepository = addressIndexRepository;
    }

    @Transactional
    public String nextReceiveAddress() {
        if (bitcoinConfig.getXpub() == null || bitcoinConfig.getXpub().isBlank()) {
            throw new IllegalStateException("bitcoin.xpub is not configured");
        }
        long index = addressIndexRepository.allocateNextIndex();
        return deriveAddress(bitcoinConfig.getXpub(), index);
    }

    private String deriveAddress(String xpub, long index) {
        if (index > Integer.MAX_VALUE) {
            throw new IllegalStateException("Derivation index overflow");
        }
        NetworkParameters params = networkParams();
        DeterministicKey accountKey = DeterministicKey.deserializeB58(xpub, params);
        DeterministicKey external = HDKeyDerivation.deriveChildKey(accountKey, ChildNumber.ZERO);
        DeterministicKey leaf =
                HDKeyDerivation.deriveChildKey(external, new ChildNumber((int) index, false));
        return SegwitAddress.fromKey(params, leaf).toString();
    }

    private NetworkParameters networkParams() {
        return switch (bitcoinConfig.getNetwork().toLowerCase()) {
            case "mainnet" -> MainNetParams.get();
            case "testnet", "test" -> TestNet3Params.get();
            default -> throw new IllegalStateException("Unsupported bitcoin.network: " + bitcoinConfig.getNetwork());
        };
    }
}
