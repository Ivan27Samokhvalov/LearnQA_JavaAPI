package lib;

import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void assertResponseTextEquals(Response response, String expectedAnswer){
        assertEquals(response.asString(), expectedAnswer, "Response text is not as expected");
    }

    public static void assertStatusCodeEquals(Response response, Integer expectedStatusCode){
        assertEquals(response.getStatusCode(), expectedStatusCode, "StatusCode is not as expected");
    }

}
