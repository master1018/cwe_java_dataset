import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InsecureTCPServer {
    public static void main(String[] args) {
        String userInput = "0.0.0.0:9999"; // User input that can control both IP address and port
        String[] parts = userInput.split(":");
        String ipAddress = parts[0];
        int port = Integer.parseInt(parts[1]);

        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ipAddress))) { // Vulnerable to CWE-15
            System.out.println("Server listening on " + ipAddress + " and port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Handle client connection here
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

