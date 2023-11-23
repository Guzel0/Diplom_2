package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class OrderApi {
    final static String CREATE_ORDER_URL = "/api/orders";
    final static String GET_USER_ORDERS_URL = "/api/orders";
    final static String GET_INGREDIENTS_URL = "/api/ingredients";
    @Step("Создание заказа")
    public static Response createOrder(String bearerToken, ArrayList<String> ingredients){
        NewOrder newOrder = new NewOrder(ingredients);
        return given()
                .headers("Content-type", "application/json","Authorization", bearerToken)
                .and()
                .body(newOrder)
                .when()
                .post(CREATE_ORDER_URL);
    }
    @Step("Получение заказов пользователя")
    public static Response getUserOrders(String bearerToken){
        return given()
                .headers("Content-type", "application/json","Authorization", bearerToken)
                .when()
                .get(GET_USER_ORDERS_URL);
    }

    @Step("Получение заказов пользователя")
    public static ArrayList getIngredients(int count){
        Response response = given()
                .headers("Content-type", "application/json")
                .when()
                .get(GET_INGREDIENTS_URL);

        IngredientsResponse ingredientsResponse = response.getBody().as(IngredientsResponse.class);
        ArrayList<String> ingredients = ingredientsResponse.getIngredients(count);
        return ingredients;
    }
}
