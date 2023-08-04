// 8. 使用ProcessBuilder执行OS命令并指定超时时间
import java.io.*;

public class OSCommandInjectionExample8 {
    public static void main(String[] args) throws IOException {
        String userInput = "example.com";
        ProcessBuilder builder = new ProcessBuilder("ping", userInput);
        Process process = builder.start();
        try {
            boolean completed = process.waitFor(5, TimeUnit.SECONDS); // 设置超时时间为5秒
            if (completed) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                reader.close();
            } else {
                System.out.println("Command timed out.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}