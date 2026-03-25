package com.yourcompany.bitcoinpayment.event;

import com.yourcompany.bitcoinpayment.domain.Payment;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishDetected(Payment payment, String txId) {
        applicationEventPublisher.publishEvent(new PaymentDetectedEvent(payment, txId));
    }

    public void publishConfirmed(Payment payment) {
        applicationEventPublisher.publishEvent(new PaymentConfirmedEvent(payment));
    }
}
