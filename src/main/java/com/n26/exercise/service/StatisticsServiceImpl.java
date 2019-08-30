package com.n26.exercise.service;

import com.n26.exercise.model.Statistics;
import com.n26.exercise.store.TransactionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private TransactionStore transactionStore;

    @Override
    public Statistics getStatistics() {
        return transactionStore.getStatistics();
    }
}
