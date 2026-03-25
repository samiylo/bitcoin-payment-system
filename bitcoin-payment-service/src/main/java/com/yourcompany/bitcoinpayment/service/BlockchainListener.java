package com.yourcompany.bitcoinpayment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.yourcompany.bitcoinpayment.client.BlockchainClient;
import com.yourcompany.bitcoinpayment.config.BitcoinConfig;
import com.yourcompany.bitcoinpayment.domain.Payment;
import com.yourcompany.bitcoinpayment.domain.PaymentStatus;
import com.yourcompany.bitcoinpayment.event.EventPublisher;
import com.yourcompany.bitcoinpayment.repository.PaymentRepository;
import com.yourcompany.bitcoinpayment.util.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BlockchainListener {

    private final BlockchainClient blockchainClient;
    private final BitcoinConfig bitcoinConfig;
    private final PaymentRepository paymentRepository;
    private final EventPublisher eventPublisher;

    public BlockchainListener(
            BlockchainClient blockchainClient,
            BitcoinConfig bitcoinConfig,
            PaymentRepository paymentRepository,
            EventPublisher eventPublisher) {
        this.blockchainClient = blockchainClient;
        this.bitcoinConfig = bitcoinConfig;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void reconcile(Payment payment) {
        Optional<Match> match = findBestMatch(payment);
        if (match.isEmpty()) {
            return;
        }
        Payment fresh = paymentRepository
                .findById(payment.getId())
                .orElseThrow(() -> new IllegalStateException("Payment vanished: " + payment.getId()));
        if (fresh.getStatus() == PaymentStatus.CONFIRMED) {
            return;
        }

        Match m = match.get();
        boolean dirty = false;

        if (fresh.getStatus() == PaymentStatus.PENDING) {
            fresh.setStatus(PaymentStatus.DETECTED);
            fresh.setTxId(m.txid());
            fresh.setConfirmations(m.confirmations());
            dirty = true;
            eventPublisher.publishDetected(fresh, m.txid());
        } else if (fresh.getStatus() == PaymentStatus.DETECTED) {
            if (fresh.getConfirmations() != m.confirmations()) {
                fresh.setConfirmations(m.confirmations());
                dirty = true;
            }
        }

        if (m.confirmations() >= bitcoinConfig.getRequiredConfirmations()
                && fresh.getStatus() != PaymentStatus.CONFIRMED) {
            fresh.setStatus(PaymentStatus.CONFIRMED);
            fresh.setConfirmations(m.confirmations());
            dirty = true;
            eventPublisher.publishConfirmed(fresh);
        }

        if (dirty) {
            fresh.setUpdatedAt(TimeUtils.nowUtc());
            paymentRepository.save(fresh);
        }
    }

    private Optional<Match> findBestMatch(Payment payment) {
        List<JsonNode> txs = blockchainClient.listAddressTransactions(payment.getBtcAddress());
        int tip = blockchainClient.getTipBlockHeight();
        Match best = null;
        for (JsonNode tx : txs) {
            long received = sumReceivedToAddress(tx, payment.getBtcAddress());
            if (received < payment.getAmountSatoshi()) {
                continue;
            }
            String txid = text(tx, "txid");
            int confirmations = confirmationsFor(tx, tip);
            if (best == null || confirmations > best.confirmations()) {
                best = new Match(txid, confirmations);
            }
        }
        return Optional.ofNullable(best);
    }

    private static long sumReceivedToAddress(JsonNode tx, String address) {
        long sum = 0;
        JsonNode vouts = tx.get("vout");
        if (vouts == null || !vouts.isArray()) {
            return 0;
        }
        for (JsonNode v : vouts) {
            if (address.equals(text(v, "scriptpubkey_address"))) {
                sum += v.path("value").asLong(0);
            }
        }
        return sum;
    }

    private static int confirmationsFor(JsonNode tx, int tipHeight) {
        JsonNode status = tx.get("status");
        if (status == null || !status.path("confirmed").asBoolean(false)) {
            return 0;
        }
        int bh = status.path("block_height").asInt(0);
        if (bh <= 0 || tipHeight <= 0) {
            return status.path("confirmed").asBoolean(false) ? 1 : 0;
        }
        return Math.max(1, tipHeight - bh + 1);
    }

    private static String text(JsonNode n, String field) {
        JsonNode v = n.get(field);
        return v == null || v.isNull() ? "" : v.asText("");
    }

    private record Match(String txid, int confirmations) {}
}
