import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InsecureDatabaseConnection {
    public static void main(String[] args) {
        String userInput = "malicious_database_url"; // User input that can be used to manipulate the database connection
        String dbUrl = "jdbc:mysql://" + userInput + "/mydb"; // Vulnerable to CWE-15

        try (Connection connection = DriverManager.getConnection(dbUrl, "username", "password")) {
            // Perform database operations here

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

