// 1. 使用FileInputStream读取绝对路径文件
import java.io.*;

public class AbsolutePathTraversalExample1 {
    public static void main(String[] args) throws IOException {
        String fileName = "C:/app/conf/application.properties";
        File file = new File(fileName);
        if (file.exists()) {
            // 未正确限制导航，允许访问C:/app/conf/application.properties文件
            FileInputStream fis = new FileInputStream(file);
            // 读取文件内容
            fis.close();
        } else {
            System.out.println("File not found.");
        }
    }
}