package com.example.service;

import com.example.exception.InsufficientFundException;
import com.example.exception.InvalidCardException;
import com.example.model.CardStatus;
import com.example.model.CreditCardPayment;
import com.example.repository.CreditCardPaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CardServiceImplTest {

    @InjectMocks
    private CardServiceImpl service;
    @Mock
    private CreditCardPaymentRepository repository;
    @Mock
    private CardTransactionService cardTransactionService;
    private CreditCardPayment creditCardPayment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        creditCardPayment = create();
    }

    @Test
    public void transactSuccess() throws InsufficientFundException {
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        when(repository.save(any(CreditCardPayment.class))).thenReturn(creditCardPayment);
        //when(cardTransactionService.save(creditCardPayment.getCardId(), creditCardPayment.getAmount(), any())).
        service.transact(creditCardPayment.getAmount(), creditCardPayment.getCardId());
    }

    @Test
    public void transactFailedWithInvalidCardException() {
        creditCardPayment.setStatus(CardStatus.CLOSED);
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        Assertions.assertThrows(InvalidCardException.class, () -> {
            service.transact(creditCardPayment.getAmount(), creditCardPayment.getCardId());
        });
    }

    @Test
    public void transactFailed() {
        creditCardPayment.setCreditLimit(100D);
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        Assertions.assertThrows(InsufficientFundException.class, () -> {
            service.transact(creditCardPayment.getAmount(), creditCardPayment.getCardId());
        });
    }

    @Test
    public void withdrawSuccess() throws InsufficientFundException {
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        when(repository.save(any(CreditCardPayment.class))).thenReturn(creditCardPayment);
        service.withdraw(creditCardPayment.getAmount(), creditCardPayment.getCardId());
    }

    @Test
    public void withdrawFailed() {
        creditCardPayment.setCashLimit(100D);
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        when(repository.save(any(CreditCardPayment.class))).thenReturn(creditCardPayment);
        Assertions.assertThrows(InsufficientFundException.class, () -> {
            service.withdraw(creditCardPayment.getAmount(), creditCardPayment.getCardId());
        });
    }

    @Test
    public void withdrawFailedWithInvalidCardException() {
        creditCardPayment.setStatus(CardStatus.CLOSED);
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        Assertions.assertThrows(InvalidCardException.class, () -> {
            service.withdraw(creditCardPayment.getAmount(), creditCardPayment.getCardId());
        });
    }

    @Test
    public void amountDueSuccess() {
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        Double actualResult = service.amountDue(creditCardPayment.getCardId());
        Assertions.assertNotNull(actualResult);
    }

    @Test
    public void amountDueFailed() {
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        Double actualResult = service.amountDue(creditCardPayment.getCardId());
        Assertions.assertEquals(actualResult, 0);
    }

    @Test
    public void amountToBePaidSuccess() {
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        double actualResult = service.amountToBePaid(creditCardPayment.getCardId());
        Assertions.assertEquals(actualResult, 4500.0);
    }

    @Test
    public void paySuccess() {
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        when(repository.save(any(CreditCardPayment.class))).thenReturn(creditCardPayment);
        service.pay(creditCardPayment.getAmount(), creditCardPayment.getCardId());
    }

    @Test
    public void eompSuccess() {
        List<CreditCardPayment> creditCardPayments = new ArrayList<>();
        creditCardPayments.add(creditCardPayment);
        when(repository.findByCardIdOrderByPaymentReceivedDateDesc(creditCardPayment.getCardId())).thenReturn(creditCardPayments);
        when(repository.save(any(CreditCardPayment.class))).thenReturn(creditCardPayment);
        service.eomp(creditCardPayment.getCardId());
    }

    private CreditCardPayment create() {
        CreditCardPayment creditCardPayment = new CreditCardPayment();
        creditCardPayment.setId(1L);
        creditCardPayment.setInterest(200D);
        creditCardPayment.setStatus(CardStatus.ACTIVE);
        creditCardPayment.setAmount(500D);
        creditCardPayment.setCardId(123456L);
        creditCardPayment.setPaymentReceivedDate(new Date());
        creditCardPayment.setCreditLimit(500D);
        creditCardPayment.setCashLimit(500D);
        creditCardPayment.setAllowedCashWithdrawal(500D);
        creditCardPayment.setMaxLimit(5000D);
        creditCardPayment.setMinDueCalcPercent(500D);
        creditCardPayment.setMinDueAmount(500D);
        creditCardPayment.setPaymentDueDate(new Date());
        creditCardPayment.setPaymentReceivedDate(new Date());
        return creditCardPayment;
    }
}
