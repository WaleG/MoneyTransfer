package com.revolut.solution.dao;

import com.revolut.solution.config.H2DataSourceProvider;
import com.revolut.solution.exception.AccountNotFoundException;
import com.revolut.solution.model.Account;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDaoImplIntegrationTest {

    private static AccountDao accountDao;

    @BeforeAll
    static void setUpOnce() {
        DataSource dataSource = new H2DataSourceProvider().get();
        accountDao = new AccountDaoImpl(dataSource);
    }

    @Test
    void whenAccountIsValidThenSavedSuccessfully() {
        // Given
        final Account account = new Account("created", "100.01");

        // When
        accountDao.create(account);
        final Account foundAccount = accountDao.getById(account.getAccountId());

        // Then
        assertAll("Account creation",
                () -> assertEquals(account.getAccountId(), foundAccount.getAccountId()),
                () -> assertEquals(account.getBalance(), foundAccount.getBalance())
        );
    }

    @Test
    void whenAccountIdDuplicatedThenExceptionThrown() {
        // Given
        final Account account1 = new Account("duplicate", "100.01");
        final Account account2 = new Account("duplicate", "1");

        // When
        accountDao.create(account1);
        Executable executable = () -> accountDao.create(account2);

        // Then
        Exception exception = assertThrows(UnableToExecuteStatementException.class, executable);
        assertTrue(exception.getMessage().startsWith("org.h2.jdbc.JdbcSQLException: Unique index or primary key violation"));
    }

    @Test
    void whenAccountIsPresentThenReturnedSuccessfully() {
        // Given
        final Account account = new Account("present", "0");
        accountDao.create(account);

        // When
        final Account foundAccount = accountDao.getById(account.getAccountId());

        // Then
        assertAll("Get account by Id",
                () -> assertEquals(account.getAccountId(), foundAccount.getAccountId()),
                () -> assertEquals(account.getBalance(), foundAccount.getBalance())
        );
    }

    @Test
    void whenAccountsArePresentThenAllReturnedSuccessfully() {
        // Given
        final Account first = new Account("first", "0");
        final Account second = new Account("second", "0");
        accountDao.create(first);
        accountDao.create(second);

        // When
        final List<Account> accounts = accountDao.getAll();

        // Then
        assertAll("Get all accounts",
                () -> assertTrue(accounts.size() >= 2),
                () -> assertTrue(accounts.contains(first)),
                () -> assertTrue(accounts.contains(second))
        );
    }

    @Test
    void whenUpdateExistingAccountThenUpdatedSuccessfully() {
        // Given
        final Account account = new Account("1", "0");
        accountDao.create(account);


        // When
        accountDao.update(new Account(account.getAccountId(), "10"));
        final Account updatedAccount = accountDao.getById(account.getAccountId());

        // Then
        assertAll("Update account",
                () -> assertEquals(account.getAccountId(), updatedAccount.getAccountId()),
                () -> assertEquals(updatedAccount.getBalance(), new BigDecimal("10"))
        );
    }

    @Test
    void whenUpdateNonExistingAccountThenExceptionThrown() {
        // When
        final Account account = new Account("10");
        Executable executable = () -> accountDao.update(account);

        // Then
        Exception exception = assertThrows(AccountNotFoundException.class, executable);
        assertTrue(exception.getMessage().startsWith("Cannot find account with id: " + account.getAccountId()));
    }
}