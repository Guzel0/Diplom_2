package org.example;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetUserOrdersTest {
    String bearerToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
    }
    @Test
    public void testWithLoginGetUserOrders(){
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String password = faker.internet().password();
        String email = faker.internet().emailAddress();
        UserApi.createUser(email, password, name);
        bearerToken = UserApi.loginUser(email, password)
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
