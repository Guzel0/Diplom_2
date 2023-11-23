package org.example;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.javafaker.Faker;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    String bearerToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
    }
    @Test
    public void testWithLoginCreateOrder(){
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String password = faker.internet().password();
        String email = faker.internet().emailAddress();
        UserApi.createUser(email, password, name);
        bearerToken = UserApi.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");

        ArrayList<String> ingredients = OrderApi.getIngredients(3);
        OrderApi.createOrder(bearerToken, ingredients)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    public void testWithoutLoginCreateOrder(){
        ArrayList<String> ingredients = OrderApi.getIngredients(3);
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
        ArrayList<String> ingredients = Constants.WRONG_INGREDIENTS;
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
