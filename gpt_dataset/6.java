import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsecureDatabaseQuery {
    public static void main(String[] args) {
        String userInput = "'; DROP TABLE users; --"; // User input that can be used to manipulate the query
        String query = "SELECT * FROM users WHERE username='" + userInput + "';"; // Vulnerable to CWE-15

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Process the query result here

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

