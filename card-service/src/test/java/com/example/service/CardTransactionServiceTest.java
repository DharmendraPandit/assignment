package com.example.service;

import com.example.model.CardTransaction;
import com.example.repository.CardTransactionRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.Date;

public class CardTransactionServiceTest {

    @InjectMocks
    private CardTransactionService service;

    @Mock
    private CardTransactionRepository repository;
    private CardTransaction cardTransaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        cardTransaction = create();
    }

    @Test
    public void saveSuccess() {
        Mockito.when(repository.save(any())).thenReturn(cardTransaction);
        service.save(cardTransaction.getCardId(), cardTransaction.getAmount(), cardTransaction.getWithdrawalType());
        //verify(service).save(any(), any(), any());
    }

    private CardTransaction create() {
        CardTransaction cardTransaction = new CardTransaction(12345L, 50000, new Date(), "$", "CR");
        return cardTransaction;
    }
}
