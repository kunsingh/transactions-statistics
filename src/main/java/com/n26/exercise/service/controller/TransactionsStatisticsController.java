package com.n26.exercise.service.controller;

import com.n26.exercise.service.model.Statistics;
import com.n26.exercise.service.model.Transaction;
import com.n26.exercise.service.service.TransactionsStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class TransactionsStatisticsController {


    @Autowired
    private TransactionsStatisticsService transactionsStatisticsService;

    @PostMapping("/transactions")
    public ResponseEntity addTransaction(@RequestBody Transaction transaction) {
        if (transactionsStatisticsService.isTransactionExpired(transaction)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            transactionsStatisticsService.addTransaction(transaction);
            return new ResponseEntity(HttpStatus.CREATED);
        }
    }

    @GetMapping("/statistics")
    public Statistics getStatistics() {
        return transactionsStatisticsService.getStatistics();
    }

}
