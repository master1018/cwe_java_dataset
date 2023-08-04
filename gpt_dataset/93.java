// 7. 使用FileWriter写入绝对路径文件
import java.io.*;

public class AbsolutePathTraversalExample7 {
    public static void main(String[] args) throws IOException {
        String fileName = "C:/logs/app.log";
        File file = new File(fileName);
        if (!file.exists()) {
            // 未正确限制导航，允许访问C:/logs/app.log文件
            FileWriter fw = new FileWriter(file);
            // 写入文件内容
            fw.write("Log data goes here.");
            fw.close();
        } else {
            System.out.println("File already exists.");
        }
    }
}