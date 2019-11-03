package com.revolut.solution.dao;

import com.revolut.solution.exception.AccountNotFoundException;
import com.revolut.solution.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Singleton
public class AccountDaoImpl implements AccountDao {

    private final Jdbi jdbi;

    @Inject
    public AccountDaoImpl(DataSource dataSource) {
        jdbi = Jdbi.create(dataSource).installPlugin(new SqlObjectPlugin());
    }

    @Override
    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    public void create(Account account) {
        jdbi.useTransaction(handle ->
                handle.createUpdate("INSERT INTO account(accountId, balance) VALUES (:accountId, :balance)")
                        .bindBean(account)
                        .execute()
        );
        log.debug("Account created: " + account);
    }

    @Override
    public Account getById(String accountId) {
        Account account = jdbi.inTransaction(handle ->
                handle.createQuery("SELECT * FROM account WHERE accountId = :accountId")
                        .bind("accountId", accountId)
                        .mapToBean(Account.class)
                        .findOne()
                        .orElseThrow(() -> new AccountNotFoundException(accountId))
        );
        log.debug("Account found: " + account);
        return account;
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = jdbi.inTransaction(handle ->
                handle.createQuery("SELECT * FROM account")
                        .mapToBean(Account.class)
                        .list()
        );
        log.debug("Accounts found: " + accounts.toString());
        return accounts;
    }

    @Override
    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    public void update(Account account) {
        jdbi.useTransaction(handle -> {
                    int updated = handle.createUpdate("UPDATE account " +
                            "SET balance = :balance " +
                            "WHERE accountId = :accountId")
                            .bindBean(account)
                            .execute();
                    if (updated < 1) throw new AccountNotFoundException(account.getAccountId());
                }
        );
        log.debug("Account updated: " + account);
    }

}
