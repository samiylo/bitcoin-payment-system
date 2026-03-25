package com.yourcompany.bitcoinpayment.scheduler;

import com.yourcompany.bitcoinpayment.config.PollingConfig;
import com.yourcompany.bitcoinpayment.domain.Payment;
import com.yourcompany.bitcoinpayment.domain.PaymentStatus;
import com.yourcompany.bitcoinpayment.repository.PaymentRepository;
import com.yourcompany.bitcoinpayment.service.BlockchainListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentPollingScheduler {

    private static final Logger log = LoggerFactory.getLogger(PaymentPollingScheduler.class);

    private final PaymentRepository paymentRepository;
    private final BlockchainListener blockchainListener;

    public PaymentPollingScheduler(PaymentRepository paymentRepository, BlockchainListener blockchainListener) {
        this.paymentRepository = paymentRepository;
        this.blockchainListener = blockchainListener;
    }

    @Scheduled(fixedDelayString = "${polling.fixed-delay-ms:30000}")
    public void pollOpenPayments() {
        List<Payment> pending = paymentRepository.findByStatusOrderByCreatedAtAsc(PaymentStatus.PENDING);
        List<Payment> detected = paymentRepository.findByStatusOrderByCreatedAtAsc(PaymentStatus.DETECTED);
        pending.forEach(this::safeReconcile);
        detected.forEach(this::safeReconcile);
    }

    private void safeReconcile(Payment payment) {
        try {
            blockchainListener.reconcile(payment);
        } catch (Exception e) {
            log.warn("Reconcile failed for payment {}: {}", payment.getId(), e.getMessage());
        }
    }
}
