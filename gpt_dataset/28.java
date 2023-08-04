import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class InsecurePropertiesFile {
    public static void main(String[] args) {
        String userInput = "key=value"; // User input that can control the content of the properties file

        Properties properties = new Properties();

        // Set properties using user-controlled input
        String[] keyValue = userInput.split("=");
        if (keyValue.length == 2) {
            String key = keyValue[0];
            String value = keyValue[1];
            properties.setProperty(key, value);
        }

        try (FileOutputStream fos = new FileOutputStream("config.properties")) { // Vulnerable to CWE-15
            properties.store(fos, "Configuration");

            // Write properties to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

