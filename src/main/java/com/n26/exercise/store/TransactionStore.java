package com.n26.exercise.store;

import com.n26.exercise.collection.ExpiringList;
import com.n26.exercise.collection.SelfExpiringList;
import com.n26.exercise.model.Statistics;
import com.n26.exercise.model.Transaction;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

@Component
public class TransactionStore {

    private static final ExpiringList<Transaction> transactions = new SelfExpiringList<>();
    private static final Statistics statistics = new Statistics();


    @PostConstruct
    public void init(){
        transactions.addExpirationListener(() -> {
            synchronized(this) {
                statistics.reset();
                if(transactions.size() > 0) {
                    statistics.setCount(transactions.size());
                    statistics.setMin(getMin().setScale(2, RoundingMode.HALF_UP));
                    statistics.setMax(getMax().setScale(2, RoundingMode.HALF_UP));
                    final BigDecimal sum = getSum();
                    statistics.setSum(sum.setScale(2, RoundingMode.HALF_UP));
                    statistics.setAvg((new BigDecimal(sum + "").divide(new BigDecimal(transactions.size()))).setScale(2, RoundingMode.HALF_UP));
                }
            }
        });
    }

    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction, transaction.getTimestamp().getTime());
    }

    public void deleteAllTransactions(){
       transactions.clear();
       statistics.reset();
    }

    public synchronized Statistics getStatistics() {
        return statistics;
    }

    private BigDecimal getMin() {
        return transactions.stream().min(Comparator.comparing(Transaction::getAmount)).get().getAmount();
    }

    private BigDecimal getMax() {
        return transactions.stream().max(Comparator.comparing(Transaction::getAmount)).get().getAmount();
    }

    private static BigDecimal getSum() {
        return transactions.stream().map(t -> t.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
