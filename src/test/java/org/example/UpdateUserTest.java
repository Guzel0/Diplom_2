package org.example;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
    String bearerToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    public void testForUpdateLoginUser(){
        UserApi.createUser("karabaeva.guzel@yandex.ru", "12345", "Guzel");
        bearerToken = UserApi.loginUser("karabaeva.guzel@yandex.ru", "12345")
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("accessToken");
        UserApi.updateUser(bearerToken, "karabaeva.guzel@yandex.ru1", "123451", "Guzel1")
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
        bearerToken = UserApi.loginUser("karabaeva.guzel@yandex.ru1", "123451")
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    public void testForUpdateLogoutUser(){
        UserApi.createUser("karabaeva.guzel@yandex.ru", "12345", "Guzel");
        UserApi.updateUser(bearerToken, "karabaeva.guzel@yandex.ru1", "123451", "Guzel1")
                .then()
                .assertThat()
                .body("message", equalTo("You should be authorised"))
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
