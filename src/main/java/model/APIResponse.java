package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APIResponse {
    private int statusCode;
    private String body;

    public APIResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getJsonValue(String key) {
        return getBody().get(key);
    }

    public JsonNode getBody() {

        try {

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readTree(body);

        } catch (Exception e) {

            return null;
        }
    }
}


