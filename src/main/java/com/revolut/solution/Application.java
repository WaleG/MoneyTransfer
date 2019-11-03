package com.revolut.solution;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.solution.config.ConfigModule;
import com.revolut.solution.controller.AccountController;
import com.revolut.solution.controller.ControllerModule;
import com.revolut.solution.controller.TransferController;
import com.revolut.solution.service.ServiceModule;
import io.javalin.Javalin;
import io.javalin.core.util.LogUtil;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Application {

    private static Javalin application;

    public static void main(String[] args) {
        application = Javalin.create(config -> {
            config.requestLogger(LogUtil::requestDevLogger);
        }).start(8080);

        Injector injector = Guice.createInjector(
                new ConfigModule(),
                new ServiceModule(),
                new ControllerModule()
        );
        AccountController accountController = injector.getInstance(AccountController.class);
        TransferController transferController = injector.getInstance(TransferController.class);

        application.routes(() -> {
            path("api/", () -> {
                path("transfer", () -> {
                    post(transferController::transferBetweenAccounts);
                });
                path("accounts", () -> {
                    post(accountController::createAccount);
                    path("/:accountId", () -> {
                        post("/withdraw", accountController::withdraw);
                        post("/deposit", accountController::deposit);
                        get(accountController::getAccountById);
                    });
                    get(accountController::getAllAccounts);
                });
            });
        });
    }

    static void start() {
        Application.main(null);
    }

    static void stop() {
        application.stop();
    }
}
