import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InsecurePropertiesFile {
    public static void main(String[] args) {
        String userInput = "config.properties"; // User input that can control the file path
        String filePath = userInput;

        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(filePath)) { // Vulnerable to CWE-15
            properties.load(fis);

            // Read properties and perform configuration here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

