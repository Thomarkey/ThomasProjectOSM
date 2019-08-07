package be.refleqt.logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonNodeHelper {
    public static JsonNode readJsonFromString(String body) {
        try {
            return new ObjectMapper().readTree(body);
        } catch (IOException e) {
            //If this is thrown it means there is no API response.
            return null;
        }
    }
}
