import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsecureURLConnection {
    public static void main(String[] args) {
        String userInput = "http://example.com"; // User input that can control the URL
        try {
            URL url = new URL(userInput); // Vulnerable to CWE-15
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Perform operations with the connection
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

