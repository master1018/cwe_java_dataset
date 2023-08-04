import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InsecureTCPServer {
    public static void main(String[] args) {
        String userInput = "127.0.0.1"; // User input that can control the IP address to bind
        String ipAddress = userInput;

        try (ServerSocket serverSocket = new ServerSocket(9999, 50, InetAddress.getByName(ipAddress))) { // Vulnerable to CWE-15
            System.out.println("Server listening on IP address " + ipAddress + " and port 9999");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Handle client connection here
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

