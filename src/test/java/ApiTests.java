import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class ApiTests {

    @Test
    public void jsonParsingTest() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn()
                .jsonPath();

        String secondMessageText = response.getString("messages[1].message");

        System.out.println(secondMessageText);
    }
}
