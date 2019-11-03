package com.revolut.solution.controller;

import com.revolut.solution.model.Account;
import com.revolut.solution.model.Operation;
import com.revolut.solution.service.AccountService;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AccountController {

    private final AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void createAccount(@NotNull Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        account = accountService.create(account);
        ctx.json(account);
        ctx.status(HttpStatus.CREATED_201);
        ctx.header("Location", buildLocationHeader(ctx.url(), account.getAccountId()));
    }

    public void getAccountById(@NotNull Context ctx) {
        String accountId = ctx.pathParam("accountId");
        Account account = accountService.getById(accountId);
        ctx.json(account);
        ctx.status(HttpStatus.OK_200);
    }

    public void getAllAccounts(@NotNull Context ctx) {
        List<Account> accounts = accountService.getAll();
        ctx.json(accounts);
        ctx.status(HttpStatus.OK_200);
    }


    public void withdraw(@NotNull Context ctx) {
        String accountId = ctx.pathParam("accountId");
        Operation operation = ctx.bodyAsClass(Operation.class);
        accountService.withdraw(accountId, operation.getAmount());
        ctx.status(HttpStatus.NO_CONTENT_204);
    }

    public void deposit(@NotNull Context ctx) {
        String accountId = ctx.pathParam("accountId");
        Operation operation = ctx.bodyAsClass(Operation.class);
        accountService.deposit(accountId, operation.getAmount());
        ctx.status(HttpStatus.NO_CONTENT_204);
    }

    private String buildLocationHeader(String url, String resource) {
        if (url.endsWith("/")) {
            return url + resource;
        } else {
            return url + "/" + resource;
        }
    }

}
