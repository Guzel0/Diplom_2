package org.example;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
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
    public void testForLoginUser(){
        UserApi.createUser(email, password, name);
        bearerToken = UserApi.loginUser(email, password)
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
        UserApi.createUser(email, password, name);
        UserApi.loginUser(email, password)
                .then()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"))
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
