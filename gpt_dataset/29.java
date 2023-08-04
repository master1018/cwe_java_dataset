import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InsecurePropertiesFile {
    public static void main(String[] args) {
        String filePath = "config.properties"; // Fixed file path for demonstration purposes

        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(filePath)) { // Vulnerable to CWE-15
            properties.load(fis);

            // Read properties and perform configuration here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

