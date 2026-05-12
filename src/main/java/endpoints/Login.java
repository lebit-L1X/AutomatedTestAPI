package endpoints;

import model.APIResponse;

import java.util.Map;


public class Login extends BaseTestMethods {

    private static final String ENDPOINT = "/auth/login";

    public APIResponse login(Map<String, Object> user) {

        return post(
                ENDPOINT,
                user,
                null,
                "email",
                "password",
                "cf_turnstile_token"
        );
    }

    public void loginAndSetToken(Map<String, Object> user) {

        APIResponse res = post(
                ENDPOINT,
                user,
                null,
                "email",
                "password",
                "cf_turnstile_token"
        );

        if (res.getStatusCode() != 200) {
            return;
        }

        String token =  res.getJsonValue("token").toString();
        setToken(token);
    }
}
