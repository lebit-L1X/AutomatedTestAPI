package model;

import org.testng.annotations.DataProvider;
import util.JSONDataProvider;

public class TestDataProvider {

    private static final String registerData = "register/register.json";
    private static final String loginData = "login/login.json";

    @DataProvider(name = "registerData")
    public static Object[][] registerData() {
        return JSONDataProvider.loadJson(registerData);
    }

    @DataProvider(name = "loginData")
    public static Object[][] loginData() {
        return JSONDataProvider.loadJson(loginData);
    }
}