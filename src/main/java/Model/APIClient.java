package Model;

import java.net.URI;
import java.net.http.*;
import java.util.Map;

public class APIClient {

    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();

    public APIClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ApiResponse sendRequest(String method, String endpoint, Map<String, String> form, String token) {
        try {

            String url = baseUrl + endpoint;

            String formData = "";
            for (Map.Entry<String, String> entry : form.entrySet()) {
                if (!formData.isEmpty()) formData += ("&");
                formData += entry.getKey();
                formData += "=";
                formData += entry.getValue();
            }

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded");

            if (token != null) {
                builder.header("Authorization", "Bearer " + token);
            }

            HttpRequest request = builder
                    .method(method, HttpRequest.BodyPublishers.ofString(formData.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return new ApiResponse(response.statusCode(), response.body());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}