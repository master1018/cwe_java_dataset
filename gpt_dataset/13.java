import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class InsecureFileWrite {
    public static void main(String[] args) {
        String filePath = "path/to/user/file"; // User input that can be used to manipulate the file path
        String content = "Hello, world!"; // Content to be written to the file

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) { // Vulnerable to CWE-15
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

