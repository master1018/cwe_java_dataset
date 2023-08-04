// 1. 使用Runtime.exec()执行OS命令
import java.io.*;

public class OSCommandInjectionExample1 {
    public static void main(String[] args) throws IOException {
        String userInput = "ping -c 3 example.com";
        String command = "ping " + userInput;
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}