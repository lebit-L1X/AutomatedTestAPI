package endpoints;

import model.APIResponse;
import util.APIClient;
import util.JSONDataProvider;

import java.util.Map;

public abstract class BaseTestClient  {

    protected static final String BASE_URL =
            "https://dunlop-warranty-be.microads.co.id/api";

    private static String authToken;
    protected APIClient api = new APIClient(BASE_URL);

    public APIResponse post(
            String endpoint,
            Map<String, Object> input,
            String bearerToken,
            String... requiredFields
    ) {

        Map<String, String> form = JSONDataProvider.formBuild(
                input,
                requiredFields
        );

        return api.sendRequest(
                "POST",
                endpoint,
                form,
                bearerToken
        );
    }

    public static void setToken(String token){
        authToken = token;
    }

    public static String getToken(){
        return authToken;
    }

}