package com.revolut.solution.controller;

import com.google.inject.AbstractModule;
import com.revolut.solution.service.AccountService;
import com.revolut.solution.service.AccountServiceImpl;
import com.revolut.solution.service.TransferService;
import com.revolut.solution.service.TransferServiceImpl;

public class ControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(TransferService.class).to(TransferServiceImpl.class);
    }
}
