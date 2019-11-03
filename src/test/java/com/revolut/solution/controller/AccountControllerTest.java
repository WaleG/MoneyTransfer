package com.revolut.solution.controller;

import com.revolut.solution.model.Account;
import com.revolut.solution.model.Operation;
import com.revolut.solution.service.AccountService;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private Context ctx;
    private AccountService accountService;
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        ctx = mock(Context.class);
        accountService = mock(AccountService.class);
        accountController = new AccountController(accountService);
    }

    @Test
    void whenCreateAccountThenReturnResponse() {
        //Given
        Account account = new Account("created", "10.01");
        when(accountService.create(account)).thenReturn(account);
        when(ctx.bodyAsClass(Account.class)).thenReturn(account);
        when(ctx.url()).thenReturn("http://localhost:8080/accounts");

        // Then
        doAnswer((i) -> {
            int status = i.getArgument(0);
            assertEquals(HttpStatus.CREATED_201, status);
            return null;
        }).when(ctx).status(anyInt());
        doAnswer((i) -> {
            Account json = i.getArgument(0);
            assertEquals(account, json);
            return null;
        }).when(ctx).json(any(Account.class));
        doAnswer((i) -> {
            String header = i.getArgument(0);
            assertEquals("Location", header);
            String value = i.getArgument(1);
            assertEquals("http://localhost:8080/accounts/" + account.getAccountId(), value);
            return null;
        }).when(ctx).header(anyString(), anyString());
        accountController.createAccount(ctx);
    }

    @Test
    void whenGetAccountByIdThenReturnResponse() {
        //Given
        Account account = new Account("created", "10.01");
        when(ctx.pathParam(anyString())).thenReturn(account.getAccountId());
        when(accountService.getById(account.getAccountId())).thenReturn(account);

        // Then
        doAnswer((i) -> {
            int status = i.getArgument(0);
            assertEquals(HttpStatus.OK_200, status);
            return null;
        }).when(ctx).status(anyInt());
        doAnswer((i) -> {
            Account json = i.getArgument(0);
            assertEquals(account, json);
            return null;
        }).when(ctx).json(any(Account.class));
        accountController.getAccountById(ctx);
    }

    @Test
    void whenGetAllAccountsThenReturnResponse() {
        //Given
        List<Account> accounts = List.of(new Account("created", "10.01"));
        when(accountService.getAll()).thenReturn(accounts);

        // Then
        doAnswer((i) -> {
            int status = i.getArgument(0);
            assertEquals(HttpStatus.OK_200, status);
            return null;
        }).when(ctx).status(anyInt());
        doAnswer((i) -> {
            List<Account> json = i.getArgument(0);
            assertEquals(accounts, json);
            return null;
        }).when(ctx).json(any(List.class));
        accountController.getAllAccounts(ctx);
    }

    @Test
    void whenWithdrawThenReturnStatus() {
        //Given
        Account account = new Account("withdrawAcc", "10.01");
        Operation operation = new Operation("100");
        when(ctx.pathParam(anyString())).thenReturn(account.getAccountId());
        when(ctx.bodyAsClass(Operation.class)).thenReturn(operation);
        when(accountService.getById(account.getAccountId())).thenReturn(account);

        // Then
        doAnswer((i) -> {
            int status = i.getArgument(0);
            assertEquals(HttpStatus.NO_CONTENT_204, status);
            return null;
        }).when(ctx).status(anyInt());
        doAnswer((i) -> {
            String accountId = i.getArgument(0);
            assertEquals(account.getAccountId(), accountId);
            BigDecimal amount = i.getArgument(1);
            assertEquals(operation.getAmount(), amount);
            return null;
        }).when(accountService).withdraw(anyString(), any(BigDecimal.class));
        accountController.withdraw(ctx);
    }

    @Test
    void whenDepositThenReturnStatus() {
        //Given
        Account account = new Account("depositAcc", "10.01");
        Operation operation = new Operation("100");
        when(ctx.pathParam(anyString())).thenReturn(account.getAccountId());
        when(ctx.bodyAsClass(Operation.class)).thenReturn(operation);
        when(accountService.getById(account.getAccountId())).thenReturn(account);

        // Then
        doAnswer((i) -> {
            int status = i.getArgument(0);
            assertEquals(HttpStatus.NO_CONTENT_204, status);
            return null;
        }).when(ctx).status(anyInt());
        doAnswer((i) -> {
            String accountId = i.getArgument(0);
            assertEquals(account.getAccountId(), accountId);
            BigDecimal amount = i.getArgument(1);
            assertEquals(operation.getAmount(), amount);
            return null;
        }).when(accountService).deposit(anyString(), any(BigDecimal.class));
        accountController.deposit(ctx);
    }
}