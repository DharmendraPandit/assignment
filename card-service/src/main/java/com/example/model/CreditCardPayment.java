package com.example.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "CARD_PAYMENT_DETAILS")
public class CreditCardPayment implements Serializable {
    private static final long serialVersionUID = 4125965356358329466L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "card_id")
    private Long cardId;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "min_due_calc_percentage")
    private Double minDueCalcPercent;
    @Column(name = "interest_rate")
    private Double interest;
    @Column(name = "min_due_amount")
    private Double minDueAmount;
    @Column(name = "max_limit")
    private Double maxLimit;
    @Column(name = "cash_limit")
    private Double cashLimit;
    @Column(name = "credit_limit")
    private Double creditLimit;
    @Column(name = "expiry_date")
    private Date expiryDate;
    @Column(name = "allowed_cash_withdrawal")
    private Double allowedCashWithdrawal;
    @Column(name = "payment_due_date")
    private Date paymentDueDate;
    @Column(name = "payment_date")
    private Date paymentMadeOn;
    @Column(name = "payment_received_date")
    private Date paymentReceivedDate;
    @Column(name = "status")
    private CardStatus status;
    @Column(name = "late_payment_charge")
    private Double latePaymentCharge;
}
