import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
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

    @Test
    public void redirectTest() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String location = response.getHeader("Location");

        System.out.println(location);
    }

    @Test
    public void longRedirectTest() {

        String location = "https://playground.learnqa.ru/api/long_redirect";

        Integer ststusCode = 301;

        while (ststusCode != 200 && location != null) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(location)
                    .andReturn();

            location = response.getHeader("Location");

            ststusCode = response.getStatusCode();

            System.out.println(location);
            System.out.println(response.getStatusCode());
        }
    }

    @Test
    public void token() throws InterruptedException {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn()
                .jsonPath();

        String token = response.getString("token");

        String seconds = response.getString("seconds");

        JsonPath responseTaskNotReady = RestAssured
                .given()
                .queryParams("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn()
                .jsonPath();

        Assertions.assertEquals("Job is NOT ready", responseTaskNotReady.getString("status"));

        Thread.sleep(Integer.parseInt(seconds) * 1000L);

        JsonPath responseTaskReady = RestAssured
                .given()
                .queryParams("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn()
                .jsonPath();

        Assertions.assertEquals("Job is ready", responseTaskReady.getString("status"));
    }
}
