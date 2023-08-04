//8. 不安全的加密传输：

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsecureDataTransmissionExample {
    public static void main(String[] args) {
        String username = "exampleUser";
        String password = "examplePassword";
        String targetURL = "http://example.com/login";

        // WARNING: This code does not use secure data transmission (no encryption)
        String response = sendDataOverHttp(targetURL, "username=" + username + "&password=" + password);

        System.out.println("Response from server: " + response);
    }

    public static String sendDataOverHttp(String targetURL, String data) {
        HttpURLConnection connection = null;
        try {
            // Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setDoOutput(true);

            // Send request
            connection.getOutputStream().write(data.getBytes());

            // Get response
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}