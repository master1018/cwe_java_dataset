import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsecureURLConnection {
    public static void main(String[] args) {
        String userInputURL = "http://example.com"; // User input that can control the URL
        String userInputMethod = "POST"; // User input that can control the request method

        try {
            URL url = new URL(userInputURL); // Vulnerable to CWE-15
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(userInputMethod); // Vulnerable to CWE-15

            // Perform operations with the connection
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

