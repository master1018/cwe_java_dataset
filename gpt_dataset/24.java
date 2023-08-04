import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InsecureTCPServer {
    public static void main(String[] args) {
        String userInput = "9999"; // User input that can control the listening port
        int port = Integer.parseInt(userInput);

        try (ServerSocket serverSocket = new ServerSocket(port)) { // Vulnerable to CWE-15
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Handle client connection here
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

