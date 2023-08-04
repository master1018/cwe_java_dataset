import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InsecureConfigurationProperty {
    public static void main(String[] args) {
        String userInput = "config.properties"; // User input that can control the configuration file path

        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(userInput)) { // Vulnerable to CWE-15
            properties.load(fis);

            // Read properties and perform configuration here
            String propertyValue = properties.getProperty("key"); // Key controlled by the user
            System.out.println("Configuration property key = " + propertyValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

