package com.example.repository;

import com.example.model.CreditCardPayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardPaymentRepository extends CrudRepository<CreditCardPayment, Long> {
    List<CreditCardPayment> findByCardIdOrderByPaymentReceivedDateDesc(long cardId);
}
