package com.n26.exercise.service.service;

import com.n26.exercise.service.model.Statistics;
import com.n26.exercise.service.model.Transaction;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public interface TransactionsStatisticsService {
    Long ONE_MINUTE_IN_MILLIS = 60 * 1000l;

    void addTransaction(Transaction transaction);
    Statistics getStatistics();

    default boolean isTransactionExpired(Transaction transaction){
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        long epochInMillis = utc.toEpochSecond() * 1000;
        return transaction.getTimestamp() < (epochInMillis - ONE_MINUTE_IN_MILLIS);
    }
}
