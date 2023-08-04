// 6. 使用RandomAccessFile读取绝对路径文件
import java.io.*;

public class AbsolutePathTraversalExample6 {
    public static void main(String[] args) throws IOException {
        String fileName = "D:/data/important.txt";
        File file = new File(fileName);
        if (file.exists()) {
            // 未正确限制导航，允许访问D:/data/important.txt文件
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            // 读取文件内容
            String line;
            while ((line = raf.readLine()) != null) {
                System.out.println(line);
            }
            raf.close();
        } else {
            System.out.println("File not found.");
        }
    }
}