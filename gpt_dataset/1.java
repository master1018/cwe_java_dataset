import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("Server listening on port 9999...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection established with " + clientSocket.getInetAddress().getHostAddress());
            
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientMessage = in.readLine();
            System.out.println("Received message from client: " + clientMessage);

            // Process the received message (e.g., write to a file, execute a command, etc.)
            // Note: In a real-world scenario, proper input validation and security checks should be performed here.

            clientSocket.close();
        }
    }
}

