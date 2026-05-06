package Model;


import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnectionManager {
    private final Dotenv dotenv = Dotenv.load();

    private final String defaultDBPort = "3306";
    private final String dbHost = dotenv.get("DB_HOST");
    private final String dbPort = dotenv.get("DB_PORT");
    private final String dbUsername = dotenv.get("DB_USERNAME");
    private final String dbPassword = dotenv.get("DB_PASSWORD");
    private final String dbName = dotenv.get("DB_DATABASE");

    public Object execQuery(String query) {

        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            int columnCount = rs.getMetaData().getColumnCount();

            //Case 1: single column → return raw string list
            if (columnCount == 1) {
                StringBuilder result = new StringBuilder();
                while (rs.next()) {
                    result.append(rs.getString(1)).append("\n");
                }
                return result.toString();
            }

            //Case 2: multiple columns → return List<Map>
            List<Map<String, Object>> results = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
            return results;
        } catch (SQLException e) {
            System.out.println("Query execution failed due to " + e);
            return null;
        }
    }

    private Connection getDbConnection() {
        Connection con;
        System.out.println("Connecting...");
        String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        try {
            con = DriverManager.getConnection(url, dbUsername, dbPassword);
        } catch (SQLException e) {
            System.out.println("Connection to DB Failed");
            throw new RuntimeException(e);
        }
        System.out.println("Connection to DB" + dbHost + " Established.");
        return con;
    }


}
