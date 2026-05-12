package Test;

import client.APIClient;

public class TestAPIClient {

    protected static final String BASE_URL =
            "https://dunlop-warranty-be.microads.co.id/api";

    protected APIClient api;
    private static String authToken;

    public TestAPIClient() {
        api = new APIClient(BASE_URL);
    }

    public static void setToken(String token){
        authToken = token;
    }

    public static String getToken(){
        return authToken;
    }

}