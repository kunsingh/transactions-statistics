package com.n26.exercise.service;

import com.n26.exercise.service.model.Transaction;
import com.n26.exercise.service.service.TransactionStatisticsServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransactionsStatisticsServiceTest {

    @Test
    public void shouldBeAbleToAddTransactionsAndCalculateStatistics(){
        TransactionStatisticsServiceImpl transactionStatisticsService = new TransactionStatisticsServiceImpl();

        Assert.assertEquals(transactionStatisticsService.getStatistics().getCount(), 0);
        Assert.assertEquals(transactionStatisticsService.getStatistics().getAvg(), 0, 0);

        double amount = 456.789;
        long timestamp = System.currentTimeMillis();
        Transaction transaction = new Transaction(amount, timestamp);

        transactionStatisticsService.addTransaction(transaction);
        transactionStatisticsService.addTransaction(transaction);

        Assert.assertEquals(transactionStatisticsService.getStatistics().getCount(), 2);
        Assert.assertEquals(transactionStatisticsService.getStatistics().getSum(), amount * 2, 0);
        Assert.assertEquals(transactionStatisticsService.getStatistics().getAvg(), amount, 0);
    }

    @Test
    public void shouldNotBeAbleToAddExpiredTransactions() {
        TransactionStatisticsServiceImpl transactionStatisticsService = new TransactionStatisticsServiceImpl();

        double amount = 123456.789;
        long timestamp = System.currentTimeMillis() - 70000;

        Transaction transaction = new Transaction(amount, timestamp);
        transactionStatisticsService.addTransaction(transaction);

        Assert.assertEquals(transactionStatisticsService.getStatistics().getCount(), 0);
    }

    @Test
    public void shouldBeAbleToGetStatisticsOnlyForTransactionsOfLastMinute() {
        TransactionStatisticsServiceImpl transactionStatisticsService = new TransactionStatisticsServiceImpl();

        long timestamp = System.currentTimeMillis();
        transactionStatisticsService.addTransaction(new Transaction(456.789, timestamp));

        transactionStatisticsService.addTransaction(new Transaction(123.45, timestamp - 50000));

        Assert.assertEquals(transactionStatisticsService.getStatistics().getCount(), 2);

        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(transactionStatisticsService.getStatistics().getCount(), 1);
    }

}
