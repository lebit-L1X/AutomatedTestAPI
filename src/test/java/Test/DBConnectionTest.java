package Test;

import Model.DatabaseConnectionManager;
import org.testng.annotations.Test;


public class DBConnectionTest {
    @Test
    public void testShowTables() {
        DatabaseConnectionManager db = new DatabaseConnectionManager();
        Object queryResult = db.execQuery("SELECT * FROM users LIMIT 5");
        System.out.println(queryResult);
    }
}
