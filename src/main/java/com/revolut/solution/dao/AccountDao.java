package com.revolut.solution.dao;

import com.revolut.solution.model.Account;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface AccountDao {
    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    void create(Account account);

    Account getById(String accountId);

    List<Account> getAll();

    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    void update(Account account);
}
