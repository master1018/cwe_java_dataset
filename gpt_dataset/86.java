// 10. 使用Runtime.exec()执行OS命令并调用waitFor()等待执行完成
import java.io.*;

public class OSCommandInjectionExample10 {
    public static void main(String[] args) throws IOException {
        String userInput = "example.com";
        String command = "ping " + userInput;
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);
        try {
            process.waitFor(); // 等待命令执行完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}