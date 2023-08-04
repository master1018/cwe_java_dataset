import java.io.File;

public class InsecureEnvironmentFilePath {
    public static void main(String[] args) {
        String fileName = "config.properties";
        String configDir = System.getenv("CONFIG_DIR"); // Vulnerable to CWE-15

        // Concatenating user-controlled data (environment variable) with a file path
        String filePath = configDir + File.separator + fileName;

        // Load and read the content of the file at filePath
        // Note: In a real-world scenario, file paths should be validated and restricted to a safe directory.

    }
}

