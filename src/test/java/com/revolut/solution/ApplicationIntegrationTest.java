package com.revolut.solution;

import com.revolut.solution.model.Account;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationIntegrationTest {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        Application.start();
    }

    @AfterAll
    static void tearDown() {
        Application.stop();
        RestAssured.reset();
    }

    @Test
    void whenCreateAccountThenReturnStatus() {
        given().body("{\n" +
                "\t\"accountId\": \"dummyAccount\",\n" +
                "\t\"balance\": \"100\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    void whenCreateAccountWithNegativeBalanceThenReturnStatus() {
        given().body("{\n" +
                "\t\"accountId\": \"dummyAccount\",\n" +
                "\t\"balance\": \"-100\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    void whenGetAccountThenReturnResponse() {
        given().body("{\n" +
                "\t\"accountId\": \"accountForGet\",\n" +
                "\t\"balance\": \"50\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
        get("/api/accounts/accountForGet").then()
                .assertThat()
                .statusCode(200)
                .body("accountId", equalTo("accountForGet"))
                .body("balance", equalTo(50));
    }

    @Test
    void whenGetAllAccountsThenReturnResponse() {
        given().body("{\n" +
                "\t\"accountId\": \"account1\",\n" +
                "\t\"balance\": \"50\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
        List<Account> accounts = List.of(get("/api/accounts").then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Account[].class));
        Account expected = new Account("account1", "50");
        assertTrue(accounts.contains(expected));
    }

    @Test
    void whenWithdrawThenReturnStatus() {
        given().body("{\n" +
                "\t\"accountId\": \"some15\",\n" +
                "\t\"balance\": \"100\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
        given().body("{\n" +
                "\t\"amount\": 20\n" +
                "}")
                .when()
                .post("api/accounts/some15/withdraw")
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    void whenDepositThenReturnStatus() {
        given().body("{\n" +
                "\t\"accountId\": \"some16\",\n" +
                "\t\"balance\": \"100\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
        given().body("{\n" +
                "\t\"amount\": 20\n" +
                "}")
                .when()
                .post("api/accounts/some15/deposit")
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    void whenMakeTransferThenReturnStatus() {
        given().body("{\n" +
                "\t\"accountId\": \"sender\",\n" +
                "\t\"balance\": \"100\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
        given().body("{\n" +
                "\t\"accountId\": \"receiver\",\n" +
                "\t\"balance\": \"100\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
        given().body("{\n" +
                "\t\"fromAccountId\": \"sender\",\n" +
                "\t\"toAccountId\": \"receiver\",\n" +
                "\t\"amount\": \"20.01\"\n" +
                "}")
                .when()
                .post("api/transfer")
                .then()
                .assertThat()
                .statusCode(204);
    }
}
