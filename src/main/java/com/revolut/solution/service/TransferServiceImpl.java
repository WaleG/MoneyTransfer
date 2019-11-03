package com.revolut.solution.service;

import com.revolut.solution.exception.SameAccountException;
import com.revolut.solution.model.UserTransaction;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransferServiceImpl implements TransferService {

    private AccountService accountService;

    @Inject
    public TransferServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void process(@NotNull UserTransaction userTransaction) {
        String fromAccountId = userTransaction.getFromAccountId();
        String toAccountId = userTransaction.getToAccountId();
        if (fromAccountId.equals(toAccountId)) {
            throw new SameAccountException(fromAccountId);
        }
        accountService.withdraw(fromAccountId, userTransaction.getAmount());
        accountService.deposit(toAccountId, userTransaction.getAmount());
    }
}
