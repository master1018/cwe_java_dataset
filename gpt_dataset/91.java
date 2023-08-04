// 5. 使用BufferedWriter写入绝对路径文件
import java.io.*;

public class AbsolutePathTraversalExample5 {
    public static void main(String[] args) throws IOException {
        String fileName = "C:/data/output.txt";
        File file = new File(fileName);
        if (!file.exists()) {
            // 未正确限制导航，允许访问C:/data/output.txt文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            // 写入文件内容
            bw.write("This is a test.");
            bw.close();
        } else {
            System.out.println("File already exists.");
        }
    }
}