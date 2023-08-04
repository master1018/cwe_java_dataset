public class InsecureEnvironmentConfig {
    public static void main(String[] args) {
        String dbPassword = System.getenv("DB_PASSWORD"); // Vulnerable to CWE-15

        // Use the dbPassword to connect to the database
        // Note: In a real-world scenario, sensitive data should be securely stored and retrieved, not through environment variables.

        // Perform database operations here
    }
}

