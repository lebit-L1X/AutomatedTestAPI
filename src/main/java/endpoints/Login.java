package endpoints;

import model.APIResponse;

import java.util.Map;


public class Login extends BaseTestClient {

    private static final String ENDPOINT = "/auth/login";

    public APIResponse login(Map<String, Object> user) {

        return post(
                ENDPOINT,
                user,
                "email",
                "password",
                "cf_turnstile_token"
        );
    }

    public String loginAndGetToken(Map<String, Object> user) {

        return post(
                ENDPOINT,
                user,
                "email",
                "password",
                "cf_turnstile_token"
        ).getJsonValue("token").toString();
    }
}
