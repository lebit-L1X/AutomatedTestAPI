package util;

import model.APIResponse;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class APIClient {

    private static final HttpClient client =
            HttpClient.newHttpClient();

    private final String baseUrl;

    public APIClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public APIResponse sendMultipartRequest(
            String method,
            String endpoint,
            Map<String, Object> form,
            String bearerToken
    ) {

        try {

            String boundary = "----JavaBoundary" + System.currentTimeMillis();

            HttpRequest.BodyPublisher bodyPublisher =
                    buildMultipartBody(form, boundary);

            HttpRequest.Builder requestBuilder =
                    HttpRequest.newBuilder()
                            .uri(URI.create(baseUrl + endpoint))
                            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                            .header("Accept", "application/json");

            if (bearerToken != null) {
                requestBuilder.header("Authorization", "Bearer " + bearerToken);
            }

            requestBuilder.method(
                    method.toUpperCase(),
                    bodyPublisher
            );

            HttpResponse<String> response =
                    client.send(
                            requestBuilder.build(),
                            HttpResponse.BodyHandlers.ofString()
                    );

            return new APIResponse(
                    response.statusCode(),
                    response.body()
            );

        } catch (Exception e) {

            return new APIResponse(
                    500,
                    "{\"error\":\"multipart request failed\"}"
            );
        }
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

    private HttpRequest.BodyPublisher buildMultipartBody(
            Map<String, Object> form,
            String boundary
    ) throws IOException {

        List<byte[]> byteArrays = new ArrayList<>();

        for (Map.Entry<String, Object> entry : form.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            StringBuilder part = new StringBuilder();

            part.append("--").append(boundary).append("\r\n");
            part.append("Content-Disposition: form-data; name=\"")
                    .append(key);

            if (value instanceof File) {

                File file = (File) value;

                part.append("\"; filename=\"")
                        .append(file.getName())
                        .append("\"\r\n");

                String mimeType = Files.probeContentType(file.toPath());

                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }

                part.append("Content-Type: ")
                        .append(mimeType)
                        .append("\r\n\r\n");

                byteArrays.add(part.toString().getBytes());

                byteArrays.add(Files.readAllBytes(file.toPath()));
                byteArrays.add("\r\n".getBytes());

            }

            else {

                part.append("\"\r\n\r\n");
                part.append(value.toString());
                part.append("\r\n");

                byteArrays.add(part.toString().getBytes());
            }
        }

        byteArrays.add(("--" + boundary + "--").getBytes());

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
}
