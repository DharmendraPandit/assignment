package com.example.controller;

import com.example.exception.InsufficientFundException;
import com.example.model.CardDetails;
import com.example.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    @Qualifier("cardService")
    private CardService service;

    @PostMapping("/transact")
    private ResponseEntity<String> transact(@RequestBody CardDetails cardDetails) throws InsufficientFundException {
        service.transact(cardDetails.getAmount(), cardDetails.getCardId());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/cash")
    private ResponseEntity<String> withdraw(@RequestBody CardDetails cardDetails) throws InsufficientFundException {
        service.withdraw(cardDetails.getAmount(), cardDetails.getCardId());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/withdraw/{cardId}")
    private ResponseEntity<Double> withdraw(@PathVariable long cardId) {
        return new ResponseEntity<>(service.amountDue(cardId), HttpStatus.OK);
    }

    @GetMapping("/bill/{cardId}")
    private ResponseEntity<Double> amountToBePaid(@PathVariable long cardId) {
        return new ResponseEntity<>(service.amountToBePaid(cardId), HttpStatus.OK);
    }

    @GetMapping("/cash/available/{cardId}")
    private ResponseEntity<Double> cashAvailable(@PathVariable long cardId) {
        return new ResponseEntity<>(service.cashAvailable(cardId), HttpStatus.OK);
    }

    @GetMapping("/credit/available/{cardId}")
    private ResponseEntity<Double> creditAvailable(@PathVariable long cardId) {
        return new ResponseEntity<>(service.creditAvailable(cardId), HttpStatus.OK);
    }

    @PostMapping("/payment/{cardId}")
    private ResponseEntity<String> pay(@RequestBody CardDetails cardDetails) {
        service.pay(cardDetails.getAmount(), cardDetails.getCardId());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/eomp/{cardId}")
    private ResponseEntity<String> eomp(@PathVariable long cardId) {
        service.eomp(cardId);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
