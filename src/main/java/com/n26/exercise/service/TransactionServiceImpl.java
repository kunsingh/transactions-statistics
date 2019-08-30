package com.n26.exercise.service;

import com.n26.exercise.model.Transaction;
import com.n26.exercise.store.TransactionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionsService {

    @Autowired
    private TransactionStore transactionStore;

    @Override
    public void addTransaction(final Transaction transaction) {
        transactionStore.addTransaction(transaction);
    }

    @Override
    public void deleteTransactions() {
        transactionStore.deleteAllTransactions();
    }
}
