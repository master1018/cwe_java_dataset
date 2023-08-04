// 3. 使用FileOutputStream写入绝对路径文件
import java.io.*;

public class AbsolutePathTraversalExample3 {
    public static void main(String[] args) throws IOException {
        String fileName = "C:/logs/application.log";
        File file = new File(fileName);
        if (!file.exists()) {
            // 未正确限制导航，允许访问C:/logs/application.log文件
            FileOutputStream fos = new FileOutputStream(file);
            // 写入文件内容
            fos.write("Hello, World!".getBytes());
            fos.close();
        } else {
            System.out.println("File already exists.");
        }
    }
}