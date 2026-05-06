package Test;

import DataProvider.RegisterData;
import Model.APIClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class AccountRegisterTest {

    APIClient api = new APIClient(
            "https://dunlop-warranty-be.microads.co.id/api"
    );

    @Test(
            dataProvider = "registerData",
            dataProviderClass = RegisterData.class
    )
    public void testRegister(Map<String, Object> user) {

        String name = user.get("name").toString();
        String email = user.get("email").toString();
        String password = user.get("password").toString();
        String phone = user.get("phone").toString();

        Map<String, String> form = Map.of(
                "name", name,
                "email", email,
                "password", password,
                "phone", phone
        );

        int responseCode = api.sendRequest(
                "POST",
                "/auth/register",
                form,
                null
        ).getStatusCode();

        Assert.assertEquals(responseCode, 201);
    }
}