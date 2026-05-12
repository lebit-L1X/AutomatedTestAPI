package util;

import model.APIResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class APIClient {

    private static final HttpClient client =
            HttpClient.newHttpClient();

    private final String baseUrl;

    public APIClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public APIResponse sendRequest(
            String method,
            String endpoint,
            Map<String, String> form,
            String bearerToken
    ) {

        try {

            String formData = buildFormData(form);

            HttpRequest.Builder requestBuilder =
                    HttpRequest.newBuilder()
                            .uri(URI.create(baseUrl + endpoint))
                            .header(
                                    "Content-Type",
                                    "application/x-www-form-urlencoded"
                            );

            if (bearerToken != null) {

                requestBuilder.header(
                        "Authorization",
                        "Bearer " + bearerToken
                );
            }

            switch (method.toUpperCase()) {

                case "POST":

                    requestBuilder.POST(
                            HttpRequest.BodyPublishers.ofString(formData)
                    );

                    break;

                case "PUT":

                    requestBuilder.PUT(
                            HttpRequest.BodyPublishers.ofString(formData)
                    );

                    break;

                default:

                    requestBuilder.GET();
            }

            HttpResponse<String> response =
                    client.send(
                            requestBuilder.build(),
                            HttpResponse.BodyHandlers.ofString()
                    );

            return new APIResponse(
                    response.statusCode(),
                    response.body()
            );

        } catch (IOException | InterruptedException e) {

            throw new RuntimeException(e);
        }
    }

    public APIResponse sendMultipartRequest(){
        return new APIResponse(1,"lol");
    }

    protected String buildFormData(
            Map<String, String> form
    ) {

        return form.entrySet()
                .stream()
                .map(entry ->
                        URLEncoder.encode(
                                entry.getKey(),
                                StandardCharsets.UTF_8
                        )
                                + "=" +
                                URLEncoder.encode(
                                        entry.getValue(),
                                        StandardCharsets.UTF_8
                                )
                )
                .collect(Collectors.joining("&"));
    }
}