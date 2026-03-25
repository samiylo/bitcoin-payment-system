package com.yourcompany.bitcoinpayment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.bitcoinpayment.domain.PaymentStatus;
import com.yourcompany.bitcoinpayment.dto.CreatePaymentRequest;
import com.yourcompany.bitcoinpayment.dto.PaymentResponse;
import com.yourcompany.bitcoinpayment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @Test
    void createReturns201() throws Exception {
        UUID id = UUID.randomUUID();
        PaymentResponse body = new PaymentResponse();
        body.setId(id);
        body.setFiatCurrency("USD");
        body.setFiatAmount(new BigDecimal("10.00"));
        body.setAmountSatoshi(15_000L);
        body.setBtcAddress("tb1qexample");
        body.setStatus(PaymentStatus.PENDING);
        body.setPaymentUri("bitcoin:tb1qexample?amount=0.00015");
        body.setQrCodePngBase64("AAA");
        body.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        when(paymentService.create(any())).thenReturn(body);

        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setFiatCurrency("USD");
        req.setFiatAmount(new BigDecimal("10.00"));

        mockMvc.perform(
                        post("/api/payments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getReturns404WhenMissing() throws Exception {
        UUID id = UUID.randomUUID();
        when(paymentService.get(id))
                .thenThrow(new com.yourcompany.bitcoinpayment.exception.PaymentNotFoundException(id));

        mockMvc.perform(get("/api/payments/{id}", id)).andExpect(status().isNotFound());
    }
}
