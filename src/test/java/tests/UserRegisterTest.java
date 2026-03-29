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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


@Epic("Authorisation cases")
@Feature("Authorisation")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Создание пользователя с некорректным email - без символа @")
    @DisplayName("Негативный тест авторизации пользователя")
    public void createUserWithIncorrectEmail(){
        Response responseCreateAuth = apiCoreRequests.postUser(
                "qwertymail.ru",
                "123",
                "Тест",
                "Тест1",
                "Тест2");

        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
        Assertions.assertStatusCodeEquals(responseCreateAuth, 400);
    }

    @ParameterizedTest(name = "Registration without required field")
    @MethodSource("provideNullRegistrationData")
    public void checkNullableFieldError(String email,
                                        String password,
                                        String username,
                                        String firstName,
                                        String lastName,
                                        String errorText,
                                        Integer statusCode){
        Response responseCreateAuth = apiCoreRequests.postUser(email, password, username, firstName, lastName);

        Assertions.assertResponseTextEquals(responseCreateAuth, errorText);
        Assertions.assertStatusCodeEquals(responseCreateAuth, statusCode);
    }
    static Stream<Arguments> provideNullRegistrationData() {
        return Stream.of(
                Arguments.of(null, "123", "Test", "Test1,", "Test2", "The following required params are missed: email", 400),
                Arguments.of(DataGenerator.getRandomEmail(), null, "Test", "Test1,", "Test2", "The following required params are missed: password", 400),
                Arguments.of(DataGenerator.getRandomEmail(), "123", null, "Test1", "Test2", "The following required params are missed: username", 400),
                Arguments.of(DataGenerator.getRandomEmail(), "123", "Test", null, "Test2", "The following required params are missed: firstName", 400),
                Arguments.of(DataGenerator.getRandomEmail(), "123", "Test", "Test1", null, "The following required params are missed: lastName", 400)
        );
    }

    @Test
    @Description("Создание пользователя с очень коротким именем в один символ")
    @DisplayName("Негативный тест авторизации пользователя")
    public void createUserWithShortName(){
        Response responseCreateAuth = apiCoreRequests.postUser(
                DataGenerator.getRandomEmail(),
                "123",
                "Т",
                "Тест1",
                "Тест2");

        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
        Assertions.assertStatusCodeEquals(responseCreateAuth, 400);
    }

    @Test
    @Description("Создание пользователя с очень длинным именем - длиннее 250 символов")
    @DisplayName("Негативный тест авторизации пользователя")
    public void createUserWithLongName(){
        Response responseCreateAuth = apiCoreRequests.postUser(
                DataGenerator.getRandomEmail(),
                "123",
                "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123" +
                        "45678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                        "901234567890123456789012345678901234567890123456789012345678901",
                "Тест1",
                "Тест2");

        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
        Assertions.assertStatusCodeEquals(responseCreateAuth, 400);
    }
}
