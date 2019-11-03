package com.revolut.solution.controller;

import com.revolut.solution.model.UserTransaction;
import com.revolut.solution.service.TransferService;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransferController {

    private TransferService transferService;

    @Inject
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    public void transferBetweenAccounts(@NotNull Context ctx) {
        UserTransaction userTransaction = ctx.bodyAsClass(UserTransaction.class);
        transferService.process(userTransaction);
        ctx.status(HttpStatus.NO_CONTENT_204);
    }
}
