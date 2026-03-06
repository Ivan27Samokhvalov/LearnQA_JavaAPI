import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloFromIvan {

    @Test
    public void getJsonHomeWorkTest() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn()
                .jsonPath();

        System.out.println(response.prettyPrint());
    }
}
