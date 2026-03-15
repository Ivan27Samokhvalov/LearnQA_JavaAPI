package lib;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    protected Map<String, String> getCookie(Response response) {
        Map<String, String> cookies = response.getCookies();

        assertTrue(cookies.size() > 0, "Response doesn't have cookie");

        return response.getCookies();
    }
}
