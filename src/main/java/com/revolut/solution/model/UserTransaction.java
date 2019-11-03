package com.revolut.solution.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Getter
public class UserTransaction {

    @NotNull
    private String fromAccountId;
    @NotNull
    private String toAccountId;
    @NotNull
    private BigDecimal amount;

    @JsonCreator
    public UserTransaction(
            @JsonProperty("fromAccountId") @NotNull String fromAccountId,
            @JsonProperty("toAccountId") @NotNull String toAccountId,
            @JsonProperty("amount") @NotNull String amount
    ) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = new BigDecimal(amount);
    }
}
