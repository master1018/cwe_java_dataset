//7. 不安全的日志记录：

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MoreInsecureLoggingExample {
    public static void main(String[] args) {
        String creditCardNumber = "1234-5678-9012-3456";

        try {
            // WARNING: Insecure logging of sensitive information
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true));
            writer.write("Credit card number: " + creditCardNumber);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}