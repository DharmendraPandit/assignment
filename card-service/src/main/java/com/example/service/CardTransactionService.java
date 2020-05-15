package com.example.service;

import com.example.model.CardTransaction;
import com.example.repository.CardTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CardTransactionService {

    @Autowired
    private CardTransactionRepository repository;

    public void save(long cardId, double amount, String withdrawalType) {
        CardTransaction cardTransaction = new CardTransaction(cardId, amount, new Date(), "$", withdrawalType);
        repository.save(cardTransaction);
    }
}
