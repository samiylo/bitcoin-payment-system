package com.yourcompany.bitcoinpayment.mapper;

import com.yourcompany.bitcoinpayment.domain.Money;
import com.yourcompany.bitcoinpayment.domain.Payment;
import com.yourcompany.bitcoinpayment.dto.PaymentResponse;
import com.yourcompany.bitcoinpayment.dto.PaymentStatusResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentMapper {

    private static final int FIAT_SCALE = 2;

    public PaymentResponse toResponse(Payment payment, String paymentUri, String qrPngBase64) {
        PaymentResponse dto = new PaymentResponse();
        dto.setId(payment.getId());
        dto.setMerchantReference(payment.getMerchantReference());
        dto.setFiatCurrency(payment.getFiatCurrency());
        dto.setFiatAmount(minorToMajor(payment.getFiatAmountMinor()));
        dto.setAmountSatoshi(payment.getAmountSatoshi());
        dto.setBtcAddress(payment.getBtcAddress());
        dto.setStatus(payment.getStatus());
        dto.setPaymentUri(paymentUri);
        dto.setQrCodePngBase64(qrPngBase64);
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }

    public PaymentStatusResponse toStatusResponse(Payment payment) {
        PaymentStatusResponse dto = new PaymentStatusResponse();
        dto.setId(payment.getId());
        dto.setStatus(payment.getStatus());
        dto.setTxId(payment.getTxId());
        dto.setConfirmations(payment.getConfirmations());
        return dto;
    }

    public Money toMoney(com.yourcompany.bitcoinpayment.dto.CreatePaymentRequest request) {
        return Money.fromMajorUnits(
                request.getFiatCurrency(),
                request.getFiatAmount(),
                FIAT_SCALE);
    }

    private BigDecimal minorToMajor(long minor) {
        return BigDecimal.valueOf(minor, FIAT_SCALE);
    }
}
