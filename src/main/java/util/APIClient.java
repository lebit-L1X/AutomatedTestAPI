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

    private final java.net.http.HttpClient client =
            java.net.http.HttpClient.newHttpClient();

    private final HttpExecutor executor =
            new HttpExecutor(client);

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

            HttpRequest.BodyPublisher body =
                    buildMultipartBody(form, boundary);

            HttpRequest.Builder builder =
                    HttpRequest.newBuilder()
                            .uri(URI.create(baseUrl + endpoint))
                            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                            .header("Accept", "application/json");

            if (bearerToken != null) {
                builder.header("Authorization", "Bearer " + bearerToken);
            }

            builder.method(method.toUpperCase(), body);

            HttpRequest request = builder.build();

            HttpResponse<String> response =
                    executor.executeWithRetry(request, 3);

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

            HttpRequest.Builder builder =
                    HttpRequest.newBuilder()
                            .uri(URI.create(baseUrl + endpoint))
                            .header("Content-Type", "application/x-www-form-urlencoded");

            if (bearerToken != null) {
                builder.header("Authorization", "Bearer " + bearerToken);
            }

            switch (method.toUpperCase()) {
                case "POST":
                    builder.POST(HttpRequest.BodyPublishers.ofString(formData));
                    break;
                case "PUT":
                    builder.PUT(HttpRequest.BodyPublishers.ofString(formData));
                    break;
                default:
                    builder.GET();
            }

            HttpRequest request = builder.build();

            HttpResponse<String> response =
                    executor.executeWithRetry(request, 3);

            return new APIResponse(
                    response.statusCode(),
                    response.body()
            );

        } catch (Exception e) {

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
