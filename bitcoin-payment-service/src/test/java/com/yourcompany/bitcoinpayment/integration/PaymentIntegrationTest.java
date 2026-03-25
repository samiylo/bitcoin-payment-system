package com.yourcompany.bitcoinpayment.integration;

import com.yourcompany.bitcoinpayment.BitcoinPaymentApplication;
import com.yourcompany.bitcoinpayment.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BitcoinPaymentApplication.class)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class PaymentIntegrationTest {

    @Test
    void contextLoads() {
        // PostgreSQL via Testcontainers + Flyway migrations
    }
}
