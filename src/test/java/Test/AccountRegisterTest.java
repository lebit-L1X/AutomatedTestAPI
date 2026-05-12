package Test;

import io.qameta.allure.*;
import model.APIResponse;
import model.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import endpoints.Register;

import java.util.Map;

import static util.JSONDataProvider.getExpectedResponseCode;

@Epic("Auth Module")
@Feature("User Registration")
public class AccountRegisterTest {

    private final Register register = new Register();

    @Test(
            dataProvider = "registerData",
            dataProviderClass = TestDataProvider.class
    )
    @Description("Register user and validate response code")
    public void testRegister(Map<String, Object> user) {

        APIResponse res = register.register(user);

        int actualResponseCode = res.getStatusCode();
        int expectedResponseCode = getExpectedResponseCode(user);

        Assert.assertEquals(
                actualResponseCode,
                expectedResponseCode,
                "Unexpected status code for: " + user.get("name") + "\nbody: " + res.getBody()
        );
    }
}