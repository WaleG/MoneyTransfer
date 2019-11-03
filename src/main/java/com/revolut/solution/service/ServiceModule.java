package com.revolut.solution.service;

import com.google.inject.AbstractModule;
import com.revolut.solution.dao.AccountDao;
import com.revolut.solution.dao.AccountDaoImpl;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountDao.class).to(AccountDaoImpl.class);
    }
}
