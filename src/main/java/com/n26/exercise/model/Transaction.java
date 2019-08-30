package com.n26.exercise.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

public class Transaction implements Comparable<Transaction>{

    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date timestamp;

    public Transaction() {}

    public Transaction(final BigDecimal amount, final Date timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Transaction o) {
        if (o.getTimestamp() == getTimestamp())
            return 0;
        else if(o.getTimestamp().getTime() > getTimestamp().getTime())
            return -1;
        else
            return 1;
    }

}
