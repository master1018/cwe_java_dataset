// 3. 使用ProcessBuilder执行OS命令并设置工作目录
import java.io.*;

public class OSCommandInjectionExample3 {
    public static void main(String[] args) throws IOException {
        String userInput = "example.com";
        ProcessBuilder builder = new ProcessBuilder("ping", userInput);
        builder.directory(new File("/tmp")); // 设置工作目录
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}