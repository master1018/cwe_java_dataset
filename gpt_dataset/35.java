import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsecureURLConnection {
    public static void main(String[] args) {
        String userInputURL = "http://example.com"; // User input that can control the URL
        String userInputHeaderName = "User-Agent"; // User input that can control the header name
        String userInputHeaderValue = "MaliciousUserAgent"; // User input that can control the header value

        try {
            URL url = new URL(userInputURL); // Vulnerable to CWE-15
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty(userInputHeaderName, userInputHeaderValue); // Vulnerable to CWE-15

            // Perform operations with the connection
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
