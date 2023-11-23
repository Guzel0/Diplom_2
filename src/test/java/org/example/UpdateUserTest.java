package org.example;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
    String bearerToken;
    String name;
    String password;
    String email;
    String updatedName;
    String updatedPassword;
    String updatedEmail;
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
        Faker faker = new Faker();
        this.name = faker.name().firstName();
        this.password = faker.internet().password();
        this.email = faker.internet().emailAddress();
        this.updatedName = faker.name().firstName();
        this.updatedPassword = faker.internet().password();
        this.updatedEmail = faker.internet().emailAddress();
    }
    @Test
    public void testForUpdateLoginUser(){
        UserApi.createUser(email, password, name);
        bearerToken = UserApi.loginUser(email, password)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("accessToken");
        UserApi.updateUser(bearerToken, updatedEmail, updatedPassword, updatedName)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
        bearerToken = UserApi.loginUser(updatedEmail, updatedPassword)
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    public void testForUpdateLogoutUser(){
        UserApi.createUser(email, password, name);
        UserApi.updateUser(bearerToken, updatedEmail, updatedPassword, updatedName)
                .then()
                .assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
        bearerToken = UserApi.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");
    }
    @After
    public void deleteUser() {
        if(bearerToken != null) {
            UserApi.deleteUser(bearerToken);
        }
    }
}
