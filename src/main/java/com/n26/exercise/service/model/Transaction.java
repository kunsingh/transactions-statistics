package com.n26.exercise.service.model;


public class Transaction implements Comparable<Transaction>{
    private double amount;
    private long timestamp;

    public Transaction() {}

    public Transaction(final double amount, final long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }


    @Override
    public int compareTo(Transaction o) {
        if (o.getTimestamp() == getTimestamp())
            return 0;
        else if(o.getTimestamp() > getTimestamp())
            return -1;
        else
            return 1;
    }

}
