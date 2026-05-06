package Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JSONDataProvider {
    private static final String BASE_PATH = "src/main/resources/data/";

    public static Object[][] loadJson(String fileName) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            File file = new File(BASE_PATH + fileName);

            List<Map<String, Object>> data = mapper.readValue(
                    file,
                    new TypeReference<List<Map<String, Object>>>() {
                    }
            );

            Object[][] result = new Object[data.size()][1];

            for (int i = 0; i < data.size(); i++) {
                result[i][0] = data.get(i);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON: " + fileName, e);
        }
    }
}