package Test;

import model.ApiResponse;
import model.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.JSONDataProvider;

import java.util.Map;

public class AccountLoginTest extends TestAPIClient{

    private String sessionToken;

    @Test(
            dataProvider = "loginData",
            dataProviderClass = TestDataProvider.class
    )
    public void testLogin(Map<String, Object> user){
        Map<String, String> form = JSONDataProvider.formBuild(
                user,
                "email",
                "password",
                "cf_turnstile_token"
        );
        int expectedOutput =  JSONDataProvider.getExpectedResponseCode(user);

        ApiResponse res = api.sendRequest(
                "POST",
                "/auth/login",
                form,
                null
        );
        int responseCode = res.getStatusCode();

        Assert.assertEquals(
                responseCode,
                expectedOutput,
                "Unexpected status code for: " + user.get("email")
        );

        if (responseCode == 200){
            AccountLoginTest.setToken(res.getJsonValue("token").toString());
            System.out.println(AccountLoginTest.getToken());
        }
    }
}
