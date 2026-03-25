package com.yourcompany.bitcoinpayment.service;

import com.yourcompany.bitcoinpayment.domain.Payment;
import com.yourcompany.bitcoinpayment.domain.PaymentStatus;
import com.yourcompany.bitcoinpayment.dto.CreatePaymentRequest;
import com.yourcompany.bitcoinpayment.dto.PaymentResponse;
import com.yourcompany.bitcoinpayment.mapper.PaymentMapper;
import com.yourcompany.bitcoinpayment.repository.PaymentRepository;
import com.yourcompany.bitcoinpayment.util.QrCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PricingService pricingService;

    @Mock
    private AddressService addressService;

    @Mock
    private QrCodeGenerator qrCodeGenerator;

    private final PaymentMapper paymentMapper = new PaymentMapper();

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(
                paymentRepository, pricingService, addressService, paymentMapper, qrCodeGenerator);
    }

    @Test
    void createPersistsPayment() {
        when(pricingService.bitcoinPriceInFiat("USD")).thenReturn(new BigDecimal("50000.00"));
        when(addressService.nextReceiveAddress()).thenReturn("tb1qtest");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));
        when(qrCodeGenerator.pngBase64(any())).thenReturn("qr");

        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setFiatCurrency("USD");
        req.setFiatAmount(new BigDecimal("50.00"));

        PaymentResponse response = paymentService.create(req);

        assertThat(response.getBtcAddress()).isEqualTo("tb1qtest");
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PENDING);

        ArgumentCaptor<Payment> cap = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(cap.capture());
        assertThat(cap.getValue().getAmountSatoshi()).isPositive();
    }

    @Test
    void getLoadsPayment() {
        UUID id = UUID.randomUUID();
        Payment p = new Payment();
        p.setId(id);
        p.setFiatCurrency("USD");
        p.setFiatAmountMinor(1000);
        p.setAmountSatoshi(2000);
        p.setBtcAddress("tb1q");
        p.setStatus(PaymentStatus.PENDING);
        p.setCreatedAt(Instant.now());
        p.setUpdatedAt(Instant.now());
        when(paymentRepository.findById(id)).thenReturn(Optional.of(p));
        when(qrCodeGenerator.pngBase64(any())).thenReturn("x");

        PaymentResponse r = paymentService.get(id);
        assertThat(r.getId()).isEqualTo(id);
    }
}
