// 4. 使用BufferedReader读取绝对路径文件
import java.io.*;

public class AbsolutePathTraversalExample4 {
    public static void main(String[] args) throws IOException {
        String fileName = "D:/data/records.txt";
        File file = new File(fileName);
        if (file.exists()) {
            // 未正确限制导航，允许访问D:/data/records.txt文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            // 读取文件内容
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } else {
            System.out.println("File not found.");
        }
    }
}