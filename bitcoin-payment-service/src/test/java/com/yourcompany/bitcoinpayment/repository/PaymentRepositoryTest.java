package com.yourcompany.bitcoinpayment.repository;

import com.yourcompany.bitcoinpayment.BitcoinPaymentApplication;
import com.yourcompany.bitcoinpayment.domain.Payment;
import com.yourcompany.bitcoinpayment.domain.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest(classes = BitcoinPaymentApplication.class)
// @ActiveProfiles("test")
// class PaymentRepositoryTest {

//     @Autowired
//     private PaymentRepository paymentRepository;

//     @Test
//     void saveAndFind() {
//         UUID id = UUID.randomUUID();
//         Instant now = Instant.now();
//         Payment p = new Payment();
//         p.setId(id);
//         p.setMerchantReference("order-1");
//         p.setFiatCurrency("USD");
//         p.setFiatAmountMinor(1234);
//         p.setAmountSatoshi(9999);
//         p.setBtcAddress("tb1qrepo");
//         p.setStatus(PaymentStatus.PENDING);
//         p.setCreatedAt(now);
//         p.setUpdatedAt(now);

//         paymentRepository.save(p);

//         assertThat(paymentRepository.findById(id)).isPresent();
//     }
// }
