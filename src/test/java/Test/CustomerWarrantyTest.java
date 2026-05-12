package Test;

import endpoints.Warranty;
import model.APIResponse;
import model.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import endpoints.Login;

import java.util.Map;

import static util.JSONDataProvider.getExpectedResponseCode;

public class CustomerWarrantyTest{

    private final Login login = new Login();
    private final Warranty warranty = new Warranty();
    @Test(
            dataProvider = "warrantyCreateData",
            dataProviderClass = TestDataProvider.class
    )
    public void testCreate(Map<String, Object> customer) {

        login.loginAndSetToken(customer);
        APIResponse res = warranty.createWarranty(customer);
        int actualResponseCode = res.getStatusCode();
        int expectedResponseCode = getExpectedResponseCode(customer);
        Assert.assertEquals(
                actualResponseCode,
                expectedResponseCode,
                "Error" + res.getStatusCode() +" for: " + customer.get("email") +
                        "\nbody: " + res.getBody()
        );
        if (actualResponseCode == expectedResponseCode) {
            System.out.println(res.getStatusCode());
            System.out.println(res.getBody());
        }

    }

}