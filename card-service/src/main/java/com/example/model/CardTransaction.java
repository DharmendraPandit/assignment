package com.example.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "CARD_TRANSACTIONS")
public class CardTransaction {
    private static final long serialVersionUID = 4125965356358329468L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private long cardId;
    private Double amount;
    @Column(name = "transation_date")
    private Date transactionDate;
    private Double availableCreditBalance;
    private String currency;
    @Column(name = "withdrawal_type")
    private String withdrawalType; // CR, DR

    public CardTransaction(long cardId, double amount, Date transactionDate, String currency, String withdrawalType) {
        this.cardId = cardId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.currency = currency;
        this.withdrawalType = withdrawalType;
    }
}
