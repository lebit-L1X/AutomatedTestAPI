package Test;

import model.APIResponse;
import model.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import endpoints.Login;

import java.util.Map;

import static util.JSONDataProvider.getExpectedResponseCode;

public class AccountLoginTest {

    private static String sessionToken = null;

    private final Login login = new Login();

    @Test(
            dataProvider = "loginData",
            dataProviderClass = TestDataProvider.class
    )
    public void testLogin(Map<String, Object> user) {

        APIResponse res = login.login(user);

        int actualResponseCode = res.getStatusCode();
        int expectedResponseCode = getExpectedResponseCode(user);

        Assert.assertEquals(
                actualResponseCode,
                expectedResponseCode,
                "Unexpected status code for: " + user.get("email")
        );
      }

}