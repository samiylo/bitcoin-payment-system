package com.yourcompany.bitcoinpayment.service;

import com.yourcompany.bitcoinpayment.domain.Money;
import com.yourcompany.bitcoinpayment.domain.Payment;
import com.yourcompany.bitcoinpayment.domain.PaymentStatus;
import com.yourcompany.bitcoinpayment.dto.CreatePaymentRequest;
import com.yourcompany.bitcoinpayment.dto.PaymentResponse;
import com.yourcompany.bitcoinpayment.dto.PaymentStatusResponse;
import com.yourcompany.bitcoinpayment.exception.PaymentNotFoundException;
import com.yourcompany.bitcoinpayment.mapper.PaymentMapper;
import com.yourcompany.bitcoinpayment.repository.PaymentRepository;
import com.yourcompany.bitcoinpayment.util.BtcConverter;
import com.yourcompany.bitcoinpayment.util.QrCodeGenerator;
import com.yourcompany.bitcoinpayment.util.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentService {

    private static final int FIAT_SCALE = 2;

    private final PaymentRepository paymentRepository;
    private final PricingService pricingService;
    private final AddressService addressService;
    private final PaymentMapper paymentMapper;
    private final QrCodeGenerator qrCodeGenerator;

    public PaymentService(
            PaymentRepository paymentRepository,
            PricingService pricingService,
            AddressService addressService,
            PaymentMapper paymentMapper,
            QrCodeGenerator qrCodeGenerator) {
        this.paymentRepository = paymentRepository;
        this.pricingService = pricingService;
        this.addressService = addressService;
        this.paymentMapper = paymentMapper;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Transactional
    public PaymentResponse create(CreatePaymentRequest request) {
        Money money = paymentMapper.toMoney(request);
        BigDecimal btcPrice = pricingService.bitcoinPriceInFiat(money.getCurrency());
        long satoshi = BtcConverter.fiatMinorToSatoshi(money.getMinorUnits(), FIAT_SCALE, btcPrice);
        String address = addressService.nextReceiveAddress();

        Instant now = TimeUtils.nowUtc();
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setMerchantReference(request.getMerchantReference());
        payment.setFiatCurrency(money.getCurrency());
        payment.setFiatAmountMinor(money.getMinorUnits());
        payment.setAmountSatoshi(satoshi);
        payment.setBtcAddress(address);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(now);
        payment.setUpdatedAt(now);

        paymentRepository.save(payment);

        String uri = bitcoinUri(address, satoshi);
        String qr = qrCodeGenerator.pngBase64(uri);
        return paymentMapper.toResponse(payment, uri, qr);
    }

    @Transactional(readOnly = true)
    public PaymentResponse get(UUID id) {
        Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
        String uri = bitcoinUri(payment.getBtcAddress(), payment.getAmountSatoshi());
        String qr = qrCodeGenerator.pngBase64(uri);
        return paymentMapper.toResponse(payment, uri, qr);
    }

    @Transactional(readOnly = true)
    public PaymentStatusResponse status(UUID id) {
        Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
        return paymentMapper.toStatusResponse(payment);
    }

    private static String bitcoinUri(String address, long satoshi) {
        BigDecimal btc = BigDecimal.valueOf(satoshi, 8).stripTrailingZeros();
        return "bitcoin:" + address + "?amount=" + btc.toPlainString();
    }

}
