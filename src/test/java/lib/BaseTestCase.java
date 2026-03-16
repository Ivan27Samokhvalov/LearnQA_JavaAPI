package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    protected Map<String, String> getCookie(Response response) {
        Map<String, String> cookies = response.getCookies();

        assertTrue(cookies.size() > 0, "Response doesn't have cookie");

        return response.getCookies();
    }

    protected Headers getHeaders(Response response){
        Headers headers = response.getHeaders();

        assertTrue(headers.exist(), "Response doesn't have headers");

        return response.getHeaders();
    }
}
