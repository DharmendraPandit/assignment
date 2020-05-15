package com.example.service;

import com.example.exception.InsufficientFundException;
import com.example.exception.InvalidCardException;
import com.example.model.CardStatus;
import com.example.model.CreditCardPayment;
import com.example.repository.CreditCardPaymentRepository;
import com.example.utility.CardUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Qualifier("cardService")
public class CardServiceImpl implements CardService {

    private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    @Value("${card.interest-rate:42}")
    private float interestRate;

    @Autowired
    private CreditCardPaymentRepository creditCardPaymentRepository;
    @Autowired
    private CardTransactionService cardTransactionService;


    @Override
    public synchronized void transact(double amount, long cardId) throws InsufficientFundException {
        List<CreditCardPayment> creditCardPayments = creditCardPaymentRepository.findByCardIdOrderByPaymentReceivedDateDesc(cardId);
        if (!creditCardPayments.isEmpty() && CardStatus.ACTIVE.equals(creditCardPayments.get(0).getStatus())) {
            CreditCardPayment creditCardPayment = creditCardPayments.get(0);
            double currCreditLimit = creditCardPayment.getCreditLimit();
            if (currCreditLimit < amount)
                throw new InsufficientFundException("Insufficient balance");
            currCreditLimit -= amount;
            creditCardPayment.setCreditLimit(currCreditLimit);
            //double allowedCashLimit = CardUtility.calculateCashWithdrawLimit(currCreditLimit, creditCardPayment.getAllowedCashWithdrawal());
            //creditCardPayment.setCashLimit(allowedCashLimit);
            creditCardPaymentRepository.save(creditCardPayment); // update credit limit
            cardTransactionService.save(cardId, amount, "DR"); // save transaction
        } else {
            throw new InvalidCardException(HttpStatus.BAD_REQUEST.value(), "transaction not permitted");
        }
    }

    @Override
    public synchronized void withdraw(double amount, long cardId) throws InsufficientFundException {
        List<CreditCardPayment> creditCardPayments = creditCardPaymentRepository.findByCardIdOrderByPaymentReceivedDateDesc(cardId);
        if (!creditCardPayments.isEmpty() && CardStatus.ACTIVE.equals(creditCardPayments.get(0).getStatus())) {
            CreditCardPayment creditCardPayment = creditCardPayments.get(0);
            double currCashLimit = creditCardPayment.getCashLimit();
            if (currCashLimit < amount)
                throw new InsufficientFundException("Insufficient balance");
            double allowedCashLimit = CardUtility.calculateCashWithdrawLimit(currCashLimit, creditCardPayment.getAllowedCashWithdrawal());
            creditCardPayment.setCashLimit(allowedCashLimit);

            creditCardPaymentRepository.save(creditCardPayment); // update cash limit
            cardTransactionService.save(cardId, amount, "DR"); // save transaction
        } else {
            throw new InvalidCardException(HttpStatus.BAD_REQUEST.value(), "with drawn not permitted");
        }
    }

    @Override
    public double amountDue(long cardId) {
        List<CreditCardPayment> creditCardPayments = creditCardPaymentRepository.findByCardIdOrderByPaymentReceivedDateDesc(cardId);
        if (!creditCardPayments.isEmpty()) {
            CreditCardPayment creditCardPayment = creditCardPayments.get(0);
            double totalAmountSpent = CardUtility.getTotalAmountSpent(creditCardPayment);
            if (totalAmountSpent < 100) {
                return totalAmountSpent;
            }
            double minCalcPercentAmount = totalAmountSpent * creditCardPayment.getMinDueCalcPercent() / 100;
            return minCalcPercentAmount < creditCardPayment.getMinDueAmount() ? creditCardPayment.getMinDueAmount() : minCalcPercentAmount; // 100 or min %age whichever is more
        }
        return 0;
    }

    @Override
    public double amountToBePaid(long cardId) {
        List<CreditCardPayment> creditCardPayments = creditCardPaymentRepository.findByCardIdOrderByPaymentReceivedDateDesc(cardId);
        if (!creditCardPayments.isEmpty()) {
            return CardUtility.getTotalAmountSpent(creditCardPayments.get(0));
        }
        return 0;
    }

    @Override
    public double cashAvailable(long cardId) {
        List<CreditCardPayment> creditCardPayments = creditCardPaymentRepository.findByCardIdOrderByPaymentReceivedDateDesc(cardId);
        if (!creditCardPayments.isEmpty()) {
            return creditCardPayments.get(0).getCashLimit();
        }
        return 0;
    }

    @Override
    public double creditAvailable(long cardId) {
        List<CreditCardPayment> creditCardPayments = creditCardPaymentRepository.findByCardIdOrderByPaymentReceivedDateDesc(cardId);
        if (!creditCardPayments.isEmpty()) {
            return creditCardPayments.get(0).getCreditLimit();
        }
        return 0;
    }

    @Override
    public void pay(double amount, long cardId) {
        // statement generated by another system
        List<CreditCardPayment> creditCardPayments = creditCardPaymentRepository.findByCardIdOrderByPaymentReceivedDateDesc(cardId);
        if (!creditCardPayments.isEmpty()) {
            CreditCardPayment creditCardPayment = creditCardPayments.get(0);

            creditCardPayment.setPaymentMadeOn(new Date());
            creditCardPayment.setPaymentReceivedDate(new Date());
            creditCardPayment.setAmount(amount);
            //
            if (amount > (CardUtility.getTotalAmountSpent(creditCardPayment) * 25 / 100)) {
                creditCardPayment.setStatus(CardStatus.ACTIVE);
            }
            creditCardPayment.setCreditLimit(creditCardPayment.getCreditLimit() + amount);

            creditCardPaymentRepository.save(creditCardPayment); // update cash limit
            cardTransactionService.save(cardId, amount, "CR"); // save transaction
        }
    }

    @Override
    public void eomp(long cardId) {
        List<CreditCardPayment> creditCardPayments = creditCardPaymentRepository.findByCardIdOrderByPaymentReceivedDateDesc(cardId);
        if (!creditCardPayments.isEmpty()) {
            CreditCardPayment creditCardPayment = creditCardPayments.get(0);
            double latePaymentAmount = CardUtility.getLatePaymentCharge(creditCardPayment);
            double interest = this.calculateCurrentInterest(creditCardPayment);
            interest += CardUtility.getInterest(creditCardPayment, interestRate);
            creditCardPayment.setInterest(interest);

            //apply interest to the statement & save in DB
            creditCardPaymentRepository.save(creditCardPayment); // update interest
            log.info("late payment amount: {}", latePaymentAmount);
            log.info("interest: " + interest);
        }
    }

    private double calculateCurrentInterest(CreditCardPayment creditCardPayment) {
        double interestAmount = 0;
        double cashSpentThisMonth = CardUtility.getTotalCashWithdrawn(creditCardPayment);
        if (cashSpentThisMonth > 0) {
            interestAmount += cashSpentThisMonth * creditCardPayment.getInterest() / 100;
        }
        return interestAmount;
    }
}
