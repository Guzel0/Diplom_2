package org.example;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    String bearerToken;
    String name;
    String password;
    String email;
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
        Faker faker = new Faker();
        this.name = faker.name().firstName();
        this.password = faker.internet().password();
        this.email = faker.internet().emailAddress();
    }
    @Test
    public void testForCreateUser(){
        UserApi.createUser(email, password, name)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
        bearerToken = UserApi.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    public void testForUniqueUser(){
        UserApi.createUser(email, password, name);
        UserApi.createUser(email, password, name)
                .then()
                .assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(HttpStatus.SC_FORBIDDEN);
        bearerToken = UserApi.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    public void testForRequiredEmail(){
        UserApi.createUser(null, password, name)
                .then()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }
    @After
    public void deleteUser() {
        if(bearerToken != null) {
            UserApi.deleteUser(bearerToken);
        }
    }
}