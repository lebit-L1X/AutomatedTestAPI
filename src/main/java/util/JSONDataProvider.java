package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.util.List;

public class JSONDataProvider {
    private static final String BASE_PATH = "src/test/resources/data/";

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

    public static Map<String, Object> multipartBuild(
            Map<String, Object> source,
            String... fields
    ) {

        Map<String, Object> result = new HashMap<>();

        for (String field : fields) {

            Object value = source.get(field);

            if (value == null) {
                continue;
            }

            if (value instanceof String) {

                String str = ((String) value).trim();

                if (str.isEmpty()) {
                    continue;
                }

                if (isFilePath(str)) {

                    File file = new File(str);

                    if (file.exists() && file.isFile()) {
                        result.put(field, file);
                    } else {
                        throw new RuntimeException(
                                "File not found: " + str + " for field: " + field
                        );
                    }

                } else {
                    result.put(field, str);
                }

            } else {
                result.put(field, value);
            }
        }

        return result;
    }

    public static Map<String, String> formBuild(
            Map<String, Object> source,
            String... fields
    ) {

        Map<String, String> result = new HashMap<>();

        for (String field : fields) {

            Object value = source.get(field);

            if (value != null) {
                result.put(field, value.toString());
            }
        }

        return result;
    }

    public static int getExpectedResponseCode(
            Map<String, Object> data
    ) {

        return getInt(data, "expected_response_code");
    }


    private static int getInt(
            Map<String, Object> data,
            String key
    ) {

        return Integer.parseInt(
                getString(data, key)
        );
    }

    private static String getString(
            Map<String, Object> data,
            String key
    ) {

        Object value = data.get(key);

        if (value == null) {
            throw new RuntimeException("Missing key: " + key);
        }

        return value.toString();
    }
    private static boolean isFilePath(String value) {

        if (value == null) return false;

        String v = value.trim();

        return v.startsWith("src/")
                || v.startsWith("file:")
                || v.contains(".jpg")
                || v.contains(".jpeg")
                || v.contains(".png")
                || v.contains(".webp")
                || v.contains(".heic")
                || v.contains(".pdf");
    }

}

