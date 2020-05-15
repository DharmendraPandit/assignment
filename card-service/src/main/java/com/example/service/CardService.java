package com.example.service;

import com.example.exception.InsufficientFundException;

public interface CardService {
    void transact(double money, long cardId) throws InsufficientFundException;

    void withdraw(double money, long cardId) throws InsufficientFundException;

    double amountDue(long cardId);

    double amountToBePaid(long cardId);

    double cashAvailable(long cardId);

    double creditAvailable(long cardId);

    void pay(double amount, long cardId);

    void eomp(long cardId);
}
