package model;

import org.testng.annotations.DataProvider;
import util.JSONDataProvider;

public class TestDataProvider {

    private static final String registerData = "register/register.json";
    private static final String loginData = "login/login.json";
    private static final String warrantyCreateData = "warranty/warranty_create.json";

    @DataProvider(name = "registerData")
    public static Object[][] registerData() {
        return JSONDataProvider.loadJson(registerData);
    }

    @DataProvider(name = "loginData")
    public static Object[][] loginData() {
        return JSONDataProvider.loadJson(loginData);
    }
    @DataProvider(name = "warrantyCreateData")
    public static Object[][] warrantyCreateData() {
        return JSONDataProvider.loadJson(warrantyCreateData);
    }
}