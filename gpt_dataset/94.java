// 1. 相对路径遍历漏洞示例
import java.io.*;

public class RelativePathTraversalExample1 {
    public static void main(String[] args) throws IOException {
        String fileName = "../conf/application.properties";
        File file = new File(fileName);
        if (file.exists()) {
            // 未正确限制导航，允许访问../conf/application.properties文件
            FileInputStream fis = new FileInputStream(file);
            // 读取文件内容
            fis.close();
        } else {
            System.out.println("File not found.");
        }
    }
}
