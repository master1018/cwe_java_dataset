import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        String serverIp = "127.0.0.1"; // Replace with the actual server IP address
        int serverPort = 9999; // Replace with the actual server port

        Socket socket = new Socket(serverIp, serverPort);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Simulate user input (This is where the vulnerability lies, as the input is not properly validated)
        String userInput = "Hello, Server!"; // Replace with any input you want to send to the server

        // Send user input to the server
        out.write(userInput);
        out.newLine();
        out.flush();

        socket.close();
    }
}

