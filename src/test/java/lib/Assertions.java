package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void assertResponseTextEquals(Response response, String expectedAnswer) {
        assertEquals(response.asString(), expectedAnswer, "Response text is not as expected");
    }

    public static void assertStatusCodeEquals(Response response, Integer expectedStatusCode) {
        assertEquals(response.getStatusCode(), expectedStatusCode, "StatusCode is not as expected");
    }

    public static void assertJsonHasNotField(Response response, String unexpectedFieldName) {
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void assertJsonHasNotFields(Response response, String[] unexpectedFieldNames) {
        for (String fieldName : unexpectedFieldNames)
            assertJsonHasNotField(response, fieldName);
    }

    public static void assertJsonHasField(Response response, String expectedFieldName) {
        response.then().assertThat().body("$", hasKey(expectedFieldName));
    }
}
