package com.yourcompany.bitcoinpayment.repository;

import com.yourcompany.bitcoinpayment.domain.Payment;
import com.yourcompany.bitcoinpayment.domain.PaymentStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends CrudRepository<Payment, UUID> {

    List<Payment> findByStatusOrderByCreatedAtAsc(PaymentStatus status);
}
