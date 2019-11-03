package com.revolut.solution.exception;

import io.javalin.http.HttpResponseException;
import org.eclipse.jetty.http.HttpStatus;

import java.util.HashMap;

public class AccountNotFoundException extends HttpResponseException {
    public AccountNotFoundException(String accountId) {
        super(HttpStatus.NOT_FOUND_404, "Cannot find account with id: " + accountId, new HashMap<>());
    }
}
