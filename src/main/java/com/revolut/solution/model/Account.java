package com.revolut.solution.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Account {
    @NotNull
    private String accountId;
    @NotNull
    private BigDecimal balance;

    public Account() {
        this.accountId = UUID.randomUUID().toString();
        this.balance = BigDecimal.ZERO;
    }

    public Account(@NotNull String balance) {
        this.accountId = UUID.randomUUID().toString();
        this.balance = new BigDecimal(balance);
    }

    public Account(@NotNull String accountId,@NotNull String balance) {
        this.accountId = accountId;
        this.balance = new BigDecimal(balance);
    }
}
