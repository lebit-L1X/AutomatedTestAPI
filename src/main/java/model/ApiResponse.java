package model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiResponse {
    private int statusCode;
    private String body;

    public ApiResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getJsonValue(String key){
        return getBody().get(key);
    }

    public JsonNode getBody() {

        try {

            ObjectMapper mapper =
                    new ObjectMapper();

            return mapper.readTree(body);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse response body as JSON",
                    e
            );
        }
    }


}