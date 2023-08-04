import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InsecureDatabaseCredentials {
    public static void main(String[] args) {
        String userInputUsername = "hacker"; // User input for username
        String userInputPassword = "123456'; DROP TABLE users; --"; // User input for password
        String dbUrl = "jdbc:mysql://localhost:3306/mydb";

        try (Connection connection = DriverManager.getConnection(dbUrl, userInputUsername, userInputPassword)) {
            // Perform database operations here

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

