import java.io.File;

public class InsecureFileDelete {
    public static void main(String[] args) {
        String fileName = "file_to_delete.txt"; // User input that can be used to manipulate the file name

        File file = new File(fileName);
        if (file.exists()) {
            file.delete(); // Vulnerable to CWE-15
        } else {
            System.out.println("File not found!");
        }
    }
}

