import java.util.Properties;

public class InsecureConfigurationProperty {
    public static void main(String[] args) {
        String userInput = "key"; // User input that can control the configuration property key

        Properties properties = new Properties();
        properties.setProperty(userInput, "value"); // Vulnerable to CWE-15

        // Store properties to a configuration file or use them for configuration
    }
}

