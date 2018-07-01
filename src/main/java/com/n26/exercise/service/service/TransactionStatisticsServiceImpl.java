package com.n26.exercise.service.service;

import com.n26.exercise.service.model.Statistics;
import com.n26.exercise.service.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionStatisticsServiceImpl implements TransactionsStatisticsService {

    private PriorityQueue<Transaction> transactions;
    private volatile Statistics statistics;

    private Lock lock = new ReentrantLock();

    public TransactionStatisticsServiceImpl(){
        transactions = new PriorityQueue<>();
        statistics = new Statistics();
    }


    @Override
    public void addTransaction(final Transaction transaction) {

        lock.lock();
        try {
            transactions.add(transaction);
            addTransactionToStatistics(transaction);
        } finally {
            lock.unlock();
        }
    }

    private void addTransactionToStatistics(final Transaction transaction) {
        statistics.setCount(statistics.getCount() + 1);
        statistics.setSum(statistics.getSum() + transaction.getAmount());
        statistics.setAvg(statistics.getSum() / statistics.getCount());
        statistics.setMin(statistics.getCount() > 1 ? Math.min(statistics.getMin(), transaction.getAmount()) : transaction.getAmount());
        statistics.setMax(statistics.getCount() > 1 ? Math.max(statistics.getMax(), transaction.getAmount()) : transaction.getAmount());
    }

    private void removeTransactionFromStatistics(final Transaction transaction) {

        if(statistics.getCount() == 1){
            statistics.reset();
        }else{
            statistics.setCount(statistics.getCount() - 1);
            statistics.setSum(statistics.getSum() - transaction.getAmount());
            statistics.setAvg(statistics.getSum() / statistics.getCount());
            statistics.setMin(getMin());
            statistics.setMax(getMax());
        }
    }

    private double getMin() {
        if(statistics.getCount() == 0){
            return 0;
        }
        return transactions
                .stream()
                .min(Comparator.comparing(Transaction::getAmount)).get().getAmount();
    }

    private double getMax() {
        if(statistics.getCount() == 0){
            return 0;
        }
        return transactions
                .stream()
                .max(Comparator.comparing(Transaction::getAmount)).get().getAmount();
    }

    @Override
    public Statistics getStatistics() {
        removeExpiredTransactions();
        return statistics;
    }

    private void removeExpiredTransactions() {
        Transaction transaction = transactions.peek();
        while (transaction != null && isTransactionExpired(transaction)) {
            lock.lock();
            try {
                transaction = transactions.peek();
                if (isTransactionExpired(transaction)) {
                    removeTransactionFromStatistics(transactions.poll());
                }
            } finally {
                lock.unlock();
            }
            transaction = transactions.peek();
        }
    }
}
