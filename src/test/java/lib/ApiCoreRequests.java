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
}
