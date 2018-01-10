package com.cournarie.codinggame.service;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * A bank account, identified by its IBAN.
 */
public class Account {
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(Account.class);
    /** Account owner. */
    private String iban;
    /** Debit history. */
    private List<Debit> history = new ArrayList<> ();
    /** Last computed amount. */
    private long lastComputed;

    /**
     * Create a new account with the given IBAN.
     * @param iban
     */
    public Account(String iban) {
        this.iban = iban;
    }

    /**
     * Check the given debit, and try to detect some fraud.
     * @param d
     * @return
     */
    public boolean check (Debit d) {
        boolean hasChanged = removeTooOld (new Date().getTime());
        history.add(d);
        if (hasChanged) {
            // recompute the full sum as history has been cleared
            lastComputed = history.stream().mapToLong(deb -> deb.getAmount()).sum();
        } else {
            // no history change, just add the new amount
            lastComputed += d.getAmount();
        }

        if (lastComputed >= FraudConfiguration.getMaxAmount()) {
            LOGGER.warn("Fraud suspected on account '" + iban + "', amount is " + lastComputed);
            return false;
        }
        return true;
    }

    /**
     * Removes from debit history debits that are too old.
     * @param now
     * @return
     */
    private boolean removeTooOld (long now) {
        return history.removeIf( deb -> deb.getTimestamp() < (now - FraudConfiguration.getElapseTime()));
    }
}
