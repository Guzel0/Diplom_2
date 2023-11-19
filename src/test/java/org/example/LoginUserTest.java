package org.example;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    String bearerToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    public void testForLoginUser(){
        UserApi.createUser("karabaeva.guzel@yandex.ru", "12345", "Guzel");
        bearerToken = UserApi.loginUser("karabaeva.guzel@yandex.ru", "12345")
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("accessToken");
    }
    @Test
    public void testForUniqueUser(){
        UserApi.createUser("karabaeva.guzel@yandex.ru", "12345", "Guzel");
        UserApi.loginUser("karabaeva.guzel@yandex", "123456")
                .then()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

        bearerToken = UserApi.loginUser("karabaeva.guzel@yandex.ru", "12345")
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
