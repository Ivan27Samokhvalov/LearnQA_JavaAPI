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

@Epic("deleteUser cases")
@Feature("deleteUser")
public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Попытка удаления пользователя по ID 2")
    @DisplayName("Негаттивный тест удаления данных пользователя")
    public void deleteUserId2() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response authResponse = apiCoreRequests.userLogin(authData);

        Response responseDeleteUser = apiCoreRequests.deleteAuthUser(
                "2",
                authResponse.getHeader("x-csrf-token"),
                authResponse.getCookie("auth_sid"));

        Assertions.assertResponseTextEquals(responseDeleteUser, "{\"error\":\"Please, do not delete test users with ID 1, 2, 3, 4 or 5.\"}");
    }

    @Test
    @Description("Удаление пользователя")
    @DisplayName("Позитивный тест удаления данных пользователя")
    public void positiveDeleteUserTest() {
        Map<String, String> newUserData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.postUser(newUserData);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", newUserData.get("email"));
        authData.put("password", newUserData.get("password"));

        Response authResponse = apiCoreRequests.userLogin(authData);

        Response responseDeleteUser = apiCoreRequests.deleteAuthUser(
                responseCreateAuth.jsonPath().get("id").toString(),
                authResponse.getHeader("x-csrf-token"),
                authResponse.getCookie("auth_sid"));

        Response getUserResponse = apiCoreRequests.getUser(responseCreateAuth.jsonPath().get("id").toString());

        Assertions.assertResponseTextEquals(getUserResponse, "User not found");
    }

    @Test
    @Description("Удаление пользователя, будучи авторизованными другим пользователем.")
    @DisplayName("Негативный тест удаления данных пользователя")
    public void deleteUserOtherAuthorizationUser() {
        Map<String, String> newUserData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.postUser(newUserData);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response authResponse = apiCoreRequests.userLogin(authData);

        Response responseDeleteUser = apiCoreRequests.deleteAuthUser(
                responseCreateAuth.jsonPath().get("id").toString(),
                authResponse.getHeader("x-csrf-token"),
                authResponse.getCookie("auth_sid"));

//         Текст ошибки противоречит здравому смыслу, но какой есть:)
        Assertions.assertResponseTextEquals(responseDeleteUser, "{\"error\":\"Please, do not delete test users with ID 1, 2, 3, 4 or 5.\"}");
    }
}
