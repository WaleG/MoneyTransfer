package com.revolut.solution.service;

import com.revolut.solution.exception.SameAccountException;
import com.revolut.solution.model.Account;
import com.revolut.solution.model.UserTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class TransferServiceImplTest {

    private AccountService accountService;
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        transferService = new TransferServiceImpl(accountService);
    }

    @Test
    void whenDifferentAccountsThenTransferProcessedSuccessfully() {
        // Given
        final Account sender = new Account("sender", "10.05");
        final Account receiver = new Account("receiver", "0");
        final UserTransaction userTransaction = new UserTransaction(
                sender.getAccountId(),
                receiver.getAccountId(),
                "10.05"
        );

        // Then
        doAnswer((i) -> {
            BigDecimal amount = i.getArgument(1);
            assertEquals(new BigDecimal("10.05"), amount);
            return null;
        }).when(accountService).withdraw("sender", sender.getBalance());
        doAnswer((i) -> {
            BigDecimal amount = i.getArgument(1);
            assertEquals(new BigDecimal("10.05"), amount);
            return null;
        }).when(accountService).deposit("receiver", sender.getBalance());
        transferService.process(userTransaction);
    }

    @Test
    void whenSameAccountsThenExceptionThrown() {
        // Given
        final Account sender = new Account("sender", "10.05");
        final Account receiver = new Account("sender", "0");
        final UserTransaction userTransaction = new UserTransaction(
                sender.getAccountId(),
                receiver.getAccountId(),
                "10.05"
        );

        // When
        Executable executable = () -> transferService.process(userTransaction);

        // Then
        Exception exception = assertThrows(SameAccountException.class, executable);
        assertTrue(exception.getMessage().startsWith("Cannot made transfer to same account: " + sender.getAccountId()));
    }
}