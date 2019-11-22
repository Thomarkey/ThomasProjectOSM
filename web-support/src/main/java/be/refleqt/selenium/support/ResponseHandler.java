package be.refleqt.selenium.support;

import static io.restassured.RestAssured.given;
import io.restassured.http.Header;

public class ResponseHandler {

    private static ResponseHandler instance;

    private ResponseHandler() {
    }

    public static ResponseHandler getInstance() {
        if (instance == null) {
            instance = new ResponseHandler();
        }
        return instance;
    }

    public Boolean isDockerReady() {
        try {
            io.restassured.response.Response response = given()
                    .header(new Header("Accept-Language", "en"))
                    .get("http://localhost:4444/wd/hub/status");

            return response.getBody().asString().contains("\"ready\": true");
        } catch (Exception e) {
            return false;
        }
    }
}
