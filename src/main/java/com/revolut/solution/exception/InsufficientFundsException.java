package com.revolut.solution.exception;

import io.javalin.http.HttpResponseException;
import org.eclipse.jetty.http.HttpStatus;

import java.math.BigDecimal;
import java.util.HashMap;

public class InsufficientFundsException extends HttpResponseException {
    public InsufficientFundsException(String accountId, BigDecimal amount) {
        super(
                HttpStatus.BAD_REQUEST_400,
                "There are not enough funds in the account " + accountId + " to withdraw " + amount,
                new HashMap<>()
        );
    }
}
