package com.revolut.solution.exception;

import io.javalin.http.HttpResponseException;
import org.eclipse.jetty.http.HttpStatus;

import java.util.HashMap;

public class SameAccountException extends HttpResponseException {
    public SameAccountException(String accountId) {
        super(HttpStatus.BAD_REQUEST_400, "Cannot made transfer to same account: " + accountId, new HashMap<>());
    }
}
