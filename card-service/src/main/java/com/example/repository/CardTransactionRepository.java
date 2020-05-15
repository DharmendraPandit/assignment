package com.example.repository;

import com.example.model.CardTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface CardTransactionRepository extends CrudRepository<CardTransaction, Long> {
    public List<CardTransaction> findByTransactionDate(Date transactionDate);
}
