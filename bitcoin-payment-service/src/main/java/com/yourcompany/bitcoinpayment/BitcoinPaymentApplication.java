package com.yourcompany.bitcoinpayment;

import com.yourcompany.bitcoinpayment.config.BitcoinConfig;
import com.yourcompany.bitcoinpayment.config.PollingConfig;
import com.yourcompany.bitcoinpayment.config.PricingConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({BitcoinConfig.class, PricingConfig.class, PollingConfig.class})
public class BitcoinPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitcoinPaymentApplication.class, args);
    }
}
