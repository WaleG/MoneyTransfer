package com.revolut.solution.service;

import com.revolut.solution.config.H2DataSourceProvider;
import com.revolut.solution.dao.AccountDao;
import com.revolut.solution.exception.InsufficientFundsException;
import com.revolut.solution.exception.NegativeAmountException;
import com.revolut.solution.model.Account;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;

@Singleton
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    @Inject
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account create(Account account) {
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeAmountException(account.getBalance());
        }
        accountDao.create(account);
        return accountDao.getById(account.getAccountId());
    }

    @Override
    public Account getById(String accountId) {
        return accountDao.getById(accountId);
    }

    @Override
    public List<Account> getAll() {
        return accountDao.getAll();
    }

    @Override
    public void withdraw(String accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeAmountException(amount);
        }
        Account account = accountDao.getById(accountId);
        BigDecimal updatedBalance = account.getBalance().subtract(amount);
        if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(account.getAccountId(), amount);
        }
        account.setBalance(updatedBalance);
        accountDao.update(account);
    }

    @Override
    public void deposit(String accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeAmountException(amount);
        }
        Account account = accountDao.getById(accountId);
        BigDecimal updatedBalance = account.getBalance().add(amount);
        account.setBalance(updatedBalance);
        accountDao.update(account);
    }
}
