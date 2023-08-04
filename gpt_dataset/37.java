//2. 不安全的日志记录：

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class InsecureLoggingExample {
    public static void main(String[] args) {
        String username = "exampleUser";
        String password = "examplePassword";

        try {
            // WARNING: Insecure logging of sensitive information
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true));
            writer.write("Login attempt - Username: " + username + ", Password: " + password);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}