package com.n26.exercise.controller;

import com.n26.exercise.model.Transaction;
import com.n26.exercise.service.TransactionsService;
import com.n26.exercise.validator.TimeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    private TransactionsService transactionsService;


    @PostMapping("/transactions")
    public ResponseEntity addTransaction(@RequestBody Transaction transaction) {
        if (TimeValidator.isInFuture(transaction.getTimestamp())) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (TimeValidator.withinLastMinute(transaction.getTimestamp().getTime())) {
            transactionsService.addTransaction(transaction);
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/transactions")
    public ResponseEntity deleteTransaction() {
        transactionsService.deleteTransactions();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Json Parse error")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleException() {
    }
}
