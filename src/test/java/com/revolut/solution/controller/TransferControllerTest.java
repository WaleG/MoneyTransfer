package com.revolut.solution.controller;

import com.revolut.solution.service.TransferService;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class TransferControllerTest {

    private Context ctx;
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        ctx = mock(Context.class);
        TransferService transferService = mock(TransferService.class);
        transferController = new TransferController(transferService);
    }

    @Test
    void whenTransferBetweenAccountsThenReturnStatus() {
        // Then
        doAnswer((i) -> {
            int status = i.getArgument(0);
            assertEquals(HttpStatus.NO_CONTENT_204, status);
            return null;
        }).when(ctx).status(anyInt());
        transferController.transferBetweenAccounts(ctx);
    }
}