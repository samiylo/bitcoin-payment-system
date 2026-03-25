package com.yourcompany.bitcoinpayment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.bitcoinpayment.client.BlockchainClient;
import com.yourcompany.bitcoinpayment.config.BitcoinConfig;
import com.yourcompany.bitcoinpayment.domain.Payment;
import com.yourcompany.bitcoinpayment.domain.PaymentStatus;
import com.yourcompany.bitcoinpayment.event.EventPublisher;
import com.yourcompany.bitcoinpayment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockchainListenerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Mock
    private BlockchainClient blockchainClient;

    @Mock
    private BitcoinConfig bitcoinConfig;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Test
    void reconcileMarksDetectedWhenFundsSeen() throws Exception {
        when(bitcoinConfig.getRequiredConfirmations()).thenReturn(1);

        UUID id = UUID.randomUUID();
        Payment pending = payment(id, PaymentStatus.PENDING, "tb1qaddr", 1000L);
        Payment fresh = payment(id, PaymentStatus.PENDING, "tb1qaddr", 1000L);

        JsonNode tx = txWithVout("tb1qaddr", 1000, true, 100);

        when(blockchainClient.listAddressTransactions("tb1qaddr")).thenReturn(List.of(tx));
        when(blockchainClient.getTipBlockHeight()).thenReturn(102);
        when(paymentRepository.findById(id)).thenReturn(Optional.of(fresh));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        BlockchainListener listener =
                new BlockchainListener(blockchainClient, bitcoinConfig, paymentRepository, eventPublisher);
        listener.reconcile(pending);

        ArgumentCaptor<Payment> cap = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(cap.capture());
        assertThat(cap.getValue().getStatus()).isEqualTo(PaymentStatus.CONFIRMED);
        verify(eventPublisher).publishDetected(any(), any());
        verify(eventPublisher).publishConfirmed(any());
    }

    @Test
    void reconcileNoOpWhenNoTx() {
        UUID id = UUID.randomUUID();
        Payment pending = payment(id, PaymentStatus.PENDING, "tb1qaddr", 1000L);
        when(blockchainClient.listAddressTransactions("tb1qaddr")).thenReturn(List.of());

        BlockchainListener listener =
                new BlockchainListener(blockchainClient, bitcoinConfig, paymentRepository, eventPublisher);
        listener.reconcile(pending);

        verify(paymentRepository, never()).save(any());
    }

    private static Payment payment(UUID id, PaymentStatus status, String addr, long sat) {
        Payment p = new Payment();
        p.setId(id);
        p.setFiatCurrency("USD");
        p.setFiatAmountMinor(100);
        p.setAmountSatoshi(sat);
        p.setBtcAddress(addr);
        p.setStatus(status);
        p.setConfirmations(0);
        return p;
    }

    private static JsonNode txWithVout(String addr, long value, boolean confirmed, int blockHeight)
            throws Exception {
        String json =
                """
                {
                  "txid": "abc123",
                  "status": { "confirmed": %s, "block_height": %d },
                  "vout": [ { "value": %d, "scriptpubkey_address": "%s" } ]
                }
                """
                        .formatted(confirmed, blockHeight, value, addr);
        return MAPPER.readTree(json);
    }
}
