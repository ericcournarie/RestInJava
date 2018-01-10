package com.cournarie.codinggame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service offering debit checking.
 */
@Service
public class AmountService {
    /** Accounts map, keyed by their IBAN. */
    private final Map<String, Account> accounts = new HashMap<> ();

    @Autowired
    public AmountService() {

    }

    /**
     * Add the given debit to given IBAN account.
     * @param accountId
     * @param d
     * @return
     */
    public boolean addAmount (String accountId, Debit d) {
        accounts.computeIfAbsent(accountId, k -> new Account (k));
        return accounts.get(accountId).check(d);
    }

}
