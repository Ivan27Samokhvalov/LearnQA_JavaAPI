package lib;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ApiCoreRequests {

    @Step("Отправить запрос на создание пользователя POST https://playground.learnqa.ru/api/user/")
    public Response postUser(String email, String password, String userName, String firstName, String lastName) {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);
        userData.put("username", userName);
        userData.put("firstName", firstName);
        userData.put("lastName", lastName);

        return RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
    }

    @Step("Отправить запрос на получение пользователя GET https://playground.learnqa.ru/api/user/{id}")
    public Response getUser(Integer id) {
        return RestAssured
                .get("https://playground.learnqa.ru/api/user/" + id)
                .andReturn();
    }

    @Step("Отправить запрос на получение пользователя GET https://playground.learnqa.ru/api/user/{id}")
    public Response getUser(String id, String header, String cookie) {
        return RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/" + id)
                .andReturn();
    }

    @Step("Авторизация пользователя POST https://playground.learnqa.ru/api/user/login")
    public Response userLogin(Map<String, String> authData) {
        return RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
    }
}
