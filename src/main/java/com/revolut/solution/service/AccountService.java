package com.revolut.solution.service;

import com.revolut.solution.model.Account;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    Account create(Account account);

    Account getById(String accountId);

    List<Account> getAll();

    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    void withdraw(String accountId, BigDecimal amount);

    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    void deposit(String accountId, BigDecimal amount);
}
