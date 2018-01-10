package com.cournarie.codinggame.service;

/**
 * An account debit.
 */
public class Debit {
    /** Time of the debit. */
    private long timestamp;
    /** Amount in euro cents. */
    private long amount;

    public Debit(long timestamp, long amount) {
        this.timestamp = timestamp;
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getAmount() {
        return amount;
    }
}
