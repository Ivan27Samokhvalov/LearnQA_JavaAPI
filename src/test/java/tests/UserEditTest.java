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

@Epic("EditUser cases")
@Feature("EditUser")
public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Изменение данных пользователя, будучи неавторизованным")
    @DisplayName("Негаттивный тест изменения данных пользователя")
    public void editUserWithoutAuthorization() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.postUser(userData);

        Map<String, String> editUserData = new HashMap<>();
        editUserData.put("firstName", "newFirstName");

        Response responseEditUser = apiCoreRequests.userEditNotAuthUser(responseCreateAuth.jsonPath().get("id"), editUserData);

        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Auth token not supplied\"}");
    }

    @Test
    @Description("Изменение данных пользователя, будучи авторизованными другим пользователем")
    @DisplayName("Негаттивный тест изменения данных пользователя")
    public void editUserAuthorizationOtherUser() {
        Map<String, String> newUserData = DataGenerator.getRegistrationData();

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseCreateAuth = apiCoreRequests.postUser(newUserData);

        Map<String, String> editUserData = new HashMap<>();
        editUserData.put("firstName", "newFirstName");

        Response authResponse = apiCoreRequests.userLogin(authData);

        Response responseEditUser = apiCoreRequests.userEditAuthUser(
                responseCreateAuth.jsonPath().get("id").toString(),
                editUserData,
                authResponse.getHeader("x-csrf-token"),
                authResponse.getCookie("auth_sid"));

        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Please, do not edit test users with ID 1, 2, 3, 4 or 5.\"}");
    }

    @Test
    @Description("Изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @")
    @DisplayName("Негаттивный тест изменения данных пользователя")
    public void editIncorrectEmail() {
        Map<String, String> newUserData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.postUser(newUserData);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", newUserData.get("email"));
        authData.put("password", newUserData.get("password"));

        Map<String, String> editUserData = new HashMap<>();
        editUserData.put("email", newUserData.get("email").replace("@", ""));

        Response authResponse = apiCoreRequests.userLogin(authData);

        Response responseEditUser = apiCoreRequests.userEditAuthUser(
                responseCreateAuth.jsonPath().get("id").toString(),
                editUserData,
                authResponse.getHeader("x-csrf-token"),
                authResponse.getCookie("auth_sid"));

        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Invalid email format\"}");
    }

    @Test
    @Description("Изменить firstName пользователя, будучи авторизованными тем же пользователем, на очень короткое значение в один символ")
    @DisplayName("Негаттивный тест изменения данных пользователя")
    public void editIncorrectFirstName() {
        Map<String, String> newUserData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.postUser(newUserData);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", newUserData.get("email"));
        authData.put("password", newUserData.get("password"));

        Map<String, String> editUserData = new HashMap<>();
        editUserData.put("firstName", "N");

        Response authResponse = apiCoreRequests.userLogin(authData);

        Response responseEditUser = apiCoreRequests.userEditAuthUser(
                responseCreateAuth.jsonPath().get("id").toString(),
                editUserData,
                authResponse.getHeader("x-csrf-token"),
                authResponse.getCookie("auth_sid"));

        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"The value for field `firstName` is too short\"}");
    }
}
