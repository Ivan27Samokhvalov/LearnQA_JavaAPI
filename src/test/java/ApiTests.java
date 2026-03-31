import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.assertj.core.api.SoftAssertions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.stream.Collectors;

public class ApiTests extends BaseTestCase {

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

    @Test
    public void passwordSelectionTest() {

        List<String> passwords = Arrays.asList(
                // 2011
                "password", "123456", "12345678", "qwerty", "abc123",
                "monkey", "1234567", "letmein", "trustno1", "dragon",
                "baseball", "111111", "iloveyou", "master", "sunshine",
                "ashley", "bailey", "passw0rd", "shadow", "123123",
                "654321", "superman", "qazwsx", "michael", "Football",
                // 2012
                "password", "123456", "12345678", "abc123", "qwerty",
                "monkey", "letmein", "dragon", "111111", "baseball",
                "iloveyou", "trustno1", "1234567", "sunshine", "master",
                "123123", "welcome", "shadow", "ashley", "football",
                "jesus", "michael", "ninja", "mustang", "password1",
                // 2013
                "123456", "password", "12345678", "qwerty", "abc123",
                "123456789", "111111", "1234567", "iloveyou", "adobe123",
                "123123", "admin", "1234567890", "letmein", "photoshop",
                "1234", "monkey", "shadow", "sunshine", "12345",
                "password1", "princess", "azerty", "trustno1", "000000",
                // 2014
                "123456", "password", "12345", "12345678", "qwerty",
                "123456789", "1234", "baseball", "dragon", "football",
                "1234567", "monkey", "letmein", "abc123", "111111",
                "mustang", "access", "shadow", "master", "michael",
                "superman", "696969", "123123", "batman", "trustno1",
                // 2015
                "123456", "password", "12345678", "qwerty", "12345",
                "123456789", "football", "1234", "1234567", "baseball",
                "welcome", "1234567890", "abc123", "111111", "1qaz2wsx",
                "dragon", "master", "monkey", "letmein", "login",
                "princess", "qwertyuiop", "solo", "passw0rd", "starwars",
                // 2016
                "123456", "password", "12345", "12345678", "football",
                "qwerty", "1234567890", "1234567", "princess", "1234",
                "login", "welcome", "solo", "abc123", "admin",
                "121212", "flower", "passw0rd", "dragon", "sunshine",
                "master", "hottie", "loveme", "zaq1zaq1", "password1",
                // 2017
                "123456", "password", "12345678", "qwerty", "12345",
                "123456789", "letmein", "1234567", "football", "iloveyou",
                "admin", "welcome", "monkey", "login", "abc123",
                "starwars", "123123", "dragon", "passw0rd", "master",
                "hello", "freedom", "whatever", "qazwsx", "trustno1",
                // 2018
                "123456", "password", "123456789", "12345678", "12345",
                "111111", "1234567", "sunshine", "qwerty", "iloveyou",
                "princess", "admin", "welcome", "666666", "abc123",
                "football", "123123", "monkey", "654321", "!@#$%^&*",
                "charlie", "aa123456", "donald", "password1", "qwerty123",
                // 2019
                "123456", "123456789", "qwerty", "password", "1234567",
                "12345678", "12345", "iloveyou", "111111", "123123",
                "abc123", "qwerty123", "1q2w3e4r", "admin", "qwertyuiop",
                "654321", "555555", "lovely", "7777777", "welcome",
                "888888", "princess", "dragon", "password1", "123qwe"
        );

        List<String> distinctPasswords = passwords.stream()
                .distinct()
                .collect(Collectors.toList());

        for (int i = 0; i < distinctPasswords.size(); i++) {

            Map<String, String> dataAuthorize = new HashMap<>();
            dataAuthorize.put("login", "super_admin");
            dataAuthorize.put("password", distinctPasswords.get(i));

            Response responseGetSecret = RestAssured
                    .given()
                    .config(RestAssuredConfig.config()
                            .sslConfig(SSLConfig.sslConfig()
                                    .relaxedHTTPSValidation()
                                    .allowAllHostnames()))
                    .body(dataAuthorize)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String responseCookie = responseGetSecret.getCookie("auth_cookie");
            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", responseCookie);


            Response responseCheckAuthCookie = RestAssured
                    .given()
                    .config(RestAssuredConfig.config()
                            .sslConfig(SSLConfig.sslConfig()
                                    .relaxedHTTPSValidation()
                                    .allowAllHostnames()))
                    .body(dataAuthorize)
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            System.out.println(distinctPasswords.get(i));
            System.out.println(responseCheckAuthCookie.asString());

            if (responseCheckAuthCookie.asString().equals("You are authorized"))

                break;
        }
    }

    @Test
    public void checkLengthStringTest() {
        String hello = "Hello, world";

        assertTrue(hello.length() > 15, "Length string < 15");
    }

    @Test
    public void fixCookieTest() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String, String> cookie = new HashMap<>(getCookie(response));
    }

    @Test
    public void fixHeaderTest() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Headers responseHeaders = getHeaders(response);
    }

    @ParameterizedTest(name = "User-agent: {0}")
    @CsvSource({
            "'Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30', 'Mobile', 'No', 'Android'",

            "'Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1', 'Mobile', 'Chrome', 'iOS'",

            "'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)', 'Googlebot', 'Unknown', 'Unknown'",

            "'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0', 'Web', 'Chrome', 'No'",

            "'Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1', 'Mobile', 'No', 'iPhone'"
    })
    public void userAgentTests(
            String userAgent,
            String expectedPlatform,
            String expectedBrowser,
            String expectedDevice
    ) {

        JsonPath response = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(response.get("platform").toString())
                .as("Unexpected platform for %s", userAgent)
                .isEqualTo(expectedPlatform);

        softly.assertThat(response.get("browser").toString())
                .as("Unexpected browser for %s", userAgent)
                .isEqualTo(expectedBrowser);

        softly.assertThat(response.get("device").toString())
                .as("Unexpected device for %s", userAgent)
                .isEqualTo(expectedDevice);

        softly.assertAll();
    }
}