package org.example;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    String bearerToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    public void testWithLoginCreateOrder(){
        UserApi.createUser("karabaeva.guzel@yandex.ru", "12345", "Guzel");
        bearerToken = UserApi.loginUser("karabaeva.guzel@yandex.ru1", "123451")
                .then()
                .extract()
                .path("accessToken");
        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f","61c0c5a71d1f82001bdaaa72"));
        OrderApi.createOrder(bearerToken, ingredients)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    public void testWithoutLoginCreateOrder(){
        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f","61c0c5a71d1f82001bdaaa72"));
        OrderApi.createOrder(bearerToken, ingredients)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    public void testWithoutIngredientsCreateOrder(){
        ArrayList<String> ingredients = new ArrayList<>();
        OrderApi.createOrder(bearerToken, ingredients)
                .then()
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    @Test
    public void testWithWrongIngredientsCreateOrder(){
        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList("60d3b41abdacab0026a733c6","609646e4dc916e00276b2870"));
        OrderApi.createOrder(bearerToken, ingredients)
                .then()
                .assertThat()
                .body("message", equalTo("One or more ids provided are incorrect"))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    @After
    public void deleteUser() {
        if(bearerToken != null) {
            UserApi.deleteUser(bearerToken);
        }
    }
}
