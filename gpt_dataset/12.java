import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InsecureFileRead {
    public static void main(String[] args) {
        String filePath = "path/to/user/file"; // User input that can be used to manipulate the file path

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // Vulnerable to CWE-15
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

