package DataProvider;

import Model.JSONDataProvider;
import org.testng.annotations.DataProvider;

public class RegisterData {

    private static final String fileName = "register.json";

    @DataProvider(name = "registerData")
    public static Object[][] registerData() {
        return JSONDataProvider.loadJson(fileName);
    }
}