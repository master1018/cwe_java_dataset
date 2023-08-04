public class DatabaseConnection {
private static final String CONNECT_STRING = "jdbc:mysql:
private Connection conn = null;
public DatabaseConnection() {
}
public void openDatabaseConnection() {
try {
conn = DriverManager.getConnection(CONNECT_STRING);
} catch (SQLException ex) {...}
}
...
}
