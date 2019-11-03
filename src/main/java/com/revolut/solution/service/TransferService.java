package com.revolut.solution.service;

import com.revolut.solution.model.UserTransaction;
import org.jdbi.v3.sqlobject.transaction.Transaction;

public interface TransferService {
    @Transaction
    void process(UserTransaction userTransaction);
}
