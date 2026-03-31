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
                .post("https://playground.learnqa.ru/api_dev/user/")
                .andReturn();
    }

    @Step("Отправить запрос на создание пользователя POST https://playground.learnqa.ru/api/user/")
    public Response postUser(Map<String, String> userData) {
        return RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api_dev/user/")
                .andReturn();
    }

    @Step("Отправить запрос на получение пользователя GET https://playground.learnqa.ru/api/user/{id}")
    public Response getUser(String id) {
        return RestAssured
                .get("https://playground.learnqa.ru/api_dev/user/" + id)
                .andReturn();
    }

    @Step("Отправить запрос на получение пользователя GET https://playground.learnqa.ru/api/user/{id}")
    public Response getUser(String id, String header, String cookie) {
        return RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api_dev/user/" + id)
                .andReturn();
    }

    @Step("Авторизация пользователя POST https://playground.learnqa.ru/api/user/login")
    public Response userLogin(Map<String, String> authData) {
        return RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api_dev/user/login")
                .andReturn();
    }

    @Step("Изменение пользователя авторизованным пользователем PUT https://playground.learnqa.ru/api/user/{id}")
    public Response userEditAuthUser(String id, Map<String, String> editUserData, String authHeader, String authCookie) {
        return RestAssured
                .given()
                .header("x-csrf-token", authHeader)
                .cookie("auth_sid", authCookie)
                .body(editUserData)
                .put("https://playground.learnqa.ru/api_dev/user/" + id)
                .andReturn();
    }

    @Step("Изменение пользователя НЕ авторизованным пользователем PUT https://playground.learnqa.ru/api/user/{id}")
    public Response userEditNotAuthUser(String id, Map<String, String> editUserData) {
        return RestAssured
                .given()
                .body(editUserData)
                .put("https://playground.learnqa.ru/api_dev/user/" + id)
                .andReturn();
    }

    @Step("Удаление пользователя авторизованным пользователем PUT https://playground.learnqa.ru/api/user/{id}")
    public Response deleteAuthUser(String id, String authHeader, String authCookie) {
        return RestAssured
                .given()
                .header("x-csrf-token", authHeader)
                .cookie("auth_sid", authCookie)
                .delete("https://playground.learnqa.ru/api_dev/user/" + id)
                .andReturn();
    }
}
