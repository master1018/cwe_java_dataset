// 2. 使用FileReader读取绝对路径文件
import java.io.*;

public class AbsolutePathTraversalExample2 {
    public static void main(String[] args) throws IOException {
        String fileName = "D:/data/users.txt";
        File file = new File(fileName);
        if (file.exists()) {
            // 未正确限制导航，允许访问D:/data/users.txt文件
            FileReader fr = new FileReader(file);
            // 读取文件内容
            fr.close();
        } else {
            System.out.println("File not found.");
        }
    }
}