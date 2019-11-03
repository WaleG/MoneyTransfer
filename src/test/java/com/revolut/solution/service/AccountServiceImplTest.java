package com.revolut.solution.service;

import com.revolut.solution.dao.AccountDao;
import com.revolut.solution.exception.InsufficientFundsException;
import com.revolut.solution.exception.NegativeAmountException;
import com.revolut.solution.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private AccountDao accountDao;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountDao = mock(AccountDao.class);
        accountService = new AccountServiceImpl(accountDao);
    }

    @Test
    void whenPositiveBalanceThenAccountCreatedSuccessfully() {
        // Given
        final Account account = new Account("created", "100.01");

        // When
        when(accountDao.getById(account.getAccountId())).thenReturn(account);
        final Account created = accountService.create(account);

        // Then
        assertAll("Account creation",
                () -> assertEquals(account.getAccountId(), created.getAccountId()),
                () -> assertEquals(account.getBalance(), created.getBalance())
        );
    }

    @Test
    void whenNegativeBalanceThenExceptionThrown() {
        // Given
        final Account account = new Account("created", "-100.01");

        // When
        Executable executable = () -> accountService.create(account);

        // Then
        Exception exception = assertThrows(NegativeAmountException.class, executable);
        assertTrue(exception.getMessage().startsWith("Cannot process negative amount: " + account.getBalance()));
    }

    @Test
    void whenGetByIdThenReturnAccount() {
        // Given
        final Account account = new Account("account", "100.01");

        // When
        when(accountDao.getById(account.getAccountId())).thenReturn(account);
        final Account accountFound = accountService.getById(account.getAccountId());

        // Then
        assertAll("Get account",
                () -> assertEquals(account.getAccountId(), accountFound.getAccountId()),
                () -> assertEquals(account.getBalance(), accountFound.getBalance())
        );
    }

    @Test
    void whenGetAllThenReturnAccounts() {
        // Given
        final Account account = new Account("created", "100.01");

        // When
        when(accountDao.getAll()).thenReturn(List.of(account));
        final List<Account> accountsFound = accountService.getAll();

        // Then
        assertTrue(accountsFound.contains(account));
    }

    @Test
    void whenWithdrawLessThanBalanceThenSuccess() {
        // Given
        final Account account = new Account("account", "10");

        // When
        when(accountDao.getById(account.getAccountId())).thenReturn(account);


        // Then
        doAnswer((i) -> {
            Account updatedAccount = i.getArgument(0);
            assertEquals(account.getAccountId(), updatedAccount.getAccountId());
            assertEquals(new BigDecimal("0.45"), updatedAccount.getBalance());
            return null;
        }).when(accountDao).update(account);
        accountService.withdraw(account.getAccountId(), new BigDecimal("9.55"));
    }

    @Test
    void whenWithdrawMoreThanBalanceThenExceptionThrown() {
        // Given
        final Account account = new Account("account", "10");
        final BigDecimal amount = new BigDecimal("10.55");

        // When
        when(accountDao.getById(account.getAccountId())).thenReturn(account);
        Executable executable = () -> accountService.withdraw(account.getAccountId(), amount);

        // Then
        Exception exception = assertThrows(InsufficientFundsException.class, executable);
        assertTrue(exception.getMessage()
                .startsWith(
                        "There are not enough funds in the account " + account.getAccountId() +
                                " to withdraw " + amount
                )
        );
    }

    @Test
    void whenWithdrawNegativeAmountThenExceptionThrown() {
        // Given
        final Account account = new Account("account", "10");
        final BigDecimal amount = new BigDecimal("-10.55");

        // When
        when(accountDao.getById(account.getAccountId())).thenReturn(account);
        Executable executable = () -> accountService.withdraw(account.getAccountId(), amount);

        // Then
        Exception exception = assertThrows(NegativeAmountException.class, executable);
        assertTrue(exception.getMessage()
                .startsWith("Cannot process negative amount: " + amount)
        );
    }

    @Test
    void whenDepositLessThanBalanceThenSuccess() {
        // Given
        final Account account = new Account("account", "10");

        // When
        when(accountDao.getById(account.getAccountId())).thenReturn(account);


        // Then
        doAnswer((i) -> {
            Account updatedAccount = i.getArgument(0);
            assertEquals(account.getAccountId(), updatedAccount.getAccountId());
            assertEquals(new BigDecimal("15.05"), updatedAccount.getBalance());
            return null;
        }).when(accountDao).update(account);
        accountService.deposit(account.getAccountId(), new BigDecimal("5.05"));
    }

    @Test
    void whenDepositNegativeAmountThenExceptionThrown() {
        // Given
        final Account account = new Account("account", "10");
        final BigDecimal amount = new BigDecimal("-10.55");

        // When
        when(accountDao.getById(account.getAccountId())).thenReturn(account);
        Executable executable = () -> accountService.deposit(account.getAccountId(), amount);

        // Then
        Exception exception = assertThrows(NegativeAmountException.class, executable);
        assertTrue(exception.getMessage()
                .startsWith("Cannot process negative amount: " + amount)
        );
    }
}