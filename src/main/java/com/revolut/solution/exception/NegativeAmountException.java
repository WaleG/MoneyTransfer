package com.revolut.solution.exception;

import io.javalin.http.HttpResponseException;
import org.eclipse.jetty.http.HttpStatus;

import java.math.BigDecimal;
import java.util.HashMap;

public class NegativeAmountException extends HttpResponseException {
    public NegativeAmountException(BigDecimal amount) {
        super(
                HttpStatus.BAD_REQUEST_400,
                "Cannot process negative amount: " + amount,
                new HashMap<>()
        );
    }
}
