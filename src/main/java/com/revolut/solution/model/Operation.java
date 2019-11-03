package com.revolut.solution.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Getter
public class Operation {
    @NotNull
    private BigDecimal amount;

    @JsonCreator
    public Operation(
            @JsonProperty("amount") @NotNull String amount
    ) {
        this.amount = new BigDecimal(amount);
    }
}
