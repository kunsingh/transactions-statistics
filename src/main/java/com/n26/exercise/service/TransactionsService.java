package com.n26.exercise.service;

import com.n26.exercise.model.Transaction;

public interface TransactionsService {

    void addTransaction(Transaction transaction);
    void deleteTransactions();

}
