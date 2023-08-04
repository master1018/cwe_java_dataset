// 7. 使用ProcessBuilder执行OS命令并传递环境变量
import java.io.*;

public class OSCommandInjectionExample7 {
    public static void main(String[] args) throws IOException {
        String userInput = "example.com";
        ProcessBuilder builder = new ProcessBuilder("ping", userInput);
        builder.environment().put("CUSTOM_VAR", "CustomValue"); // 设置环境变量
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}