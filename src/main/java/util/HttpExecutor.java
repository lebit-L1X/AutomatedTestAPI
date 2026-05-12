package util;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpExecutor {

    private final java.net.http.HttpClient client;

    public HttpExecutor(java.net.http.HttpClient client) {
        this.client = client;
    }

    public HttpResponse<String> executeWithRetry(
            HttpRequest request,
            int maxRetries
    ) throws IOException, InterruptedException {

        int attempt = 0;

        while (true) {

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // NOT rate limited → return immediately
            if (response.statusCode() != 429) {
                return response;
            }

            attempt++;

            if (attempt > maxRetries) {
                return response;
            }

            String retryAfter = response.headers()
                    .firstValue("Retry-After")
                    .orElse("1");

            long waitSeconds;

            try {
                waitSeconds = Long.parseLong(retryAfter);
            } catch (Exception e) {
                waitSeconds = 1;
            }

            System.out.println("429 received. retrying in " + waitSeconds + "s");

            Thread.sleep(waitSeconds * 1000);
        }
    }
}