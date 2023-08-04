// 9. 使用Runtime.exec()执行OS命令并处理异常流
import java.io.*;

public class OSCommandInjectionExample9 {
    public static void main(String[] args) throws IOException {
        String userInput = "example.com";
        String command = "ping " + userInput;
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(command);
            // 处理异常输出流
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
            }
            errorReader.close();
            // 处理标准输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
}