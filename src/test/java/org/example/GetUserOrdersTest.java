package org.example;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;

public class GetUserOrdersTest {
    String bearerToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    public void testWithLoginGetUserOrders(){
        UserApi.createUser("karabaeva.guzel@yandex.ru", "12345", "Guzel");
        bearerToken = UserApi.loginUser("karabaeva.guzel@yandex.ru", "12345")
                .then()
                .extract()
                .path("accessToken");
        OrderApi.getUserOrders(bearerToken)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    public void testWithoutLoginGetUserOrders(){
        OrderApi.getUserOrders(bearerToken)
                .then()
                .assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
    @After
    public void deleteUser() {
        if(bearerToken != null) {
            UserApi.deleteUser(bearerToken);
        }
    }
}
