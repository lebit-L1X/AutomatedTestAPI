package endpoints;

import model.APIResponse;

import java.util.Map;

public class Register extends BaseTestClient {

    private static final String ENDPOINT = "/auth/register";

    public APIResponse register(Map<String, Object> user) {

        return post(
                ENDPOINT,
                user,
                null,
                "name",
                "email",
                "password",
                "phone"
        );
    }
}