package org.example;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    String bearerToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    public void testForCreateUser(){
        UserApi.createUser("karabaeva.guzel@yandex.ru", "12345", "Guzel")
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
        bearerToken = UserApi.loginUser("karabaeva.guzel@yandex.ru", "12345")
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    public void testForUniqueUser(){
        UserApi.createUser("karabaeva.guzel@yandex.ru", "12345", "Guzel");
        UserApi.createUser("karabaeva.guzel@yandex.ru", "12345", "Guzel")
                .then()
                .assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(HttpStatus.SC_FORBIDDEN);
        bearerToken = UserApi.loginUser("karabaeva.guzel@yandex.ru", "12345")
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    public void testForRequiredEmail(){
        UserApi.createUser(null, "12345", "Guzel")
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