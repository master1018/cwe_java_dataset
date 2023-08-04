// 5. 使用ProcessBuilder执行OS命令并重定向输出
import java.io.*;

public class OSCommandInjectionExample5 {
    public static void main(String[] args) throws IOException {
        String userInput = "example.com";
        ProcessBuilder builder = new ProcessBuilder("ping", userInput);
        builder.redirectOutput(new File("/tmp/output.txt")); // 重定向输出到文件
        Process process = builder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Command executed successfully.");
    }
}