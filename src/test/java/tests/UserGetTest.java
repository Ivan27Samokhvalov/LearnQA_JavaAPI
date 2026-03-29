package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorisation cases")
@Feature("Authorisation")
public class UserGetTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Авторизованный пользователь получает данные другого пользователя")
    @DisplayName("Позитивный тест получения данных пользователя")
    public void testGetUserDataOtherUseer() {
        Response newUserData = apiCoreRequests.postUser(
                DataGenerator.getRandomEmail(),
                "123",
                "Test",
                "Test1",
                "Test2");


        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response authResponse = apiCoreRequests.userLogin(authData);

        Response userData = apiCoreRequests.getUser(
                newUserData.jsonPath().get("id").toString(),
                authResponse.getHeader("x-csrf-token"),
                authResponse.getCookie("auth_sid"));

        String[] unexpectedFields = {"id", "email", "firstName", "lastName"};

        Assertions.assertJsonHasNotFields(userData, unexpectedFields);
        Assertions.assertJsonHasField(userData, "username");
    }
}
