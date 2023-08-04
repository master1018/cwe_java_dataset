//5. 不安全的文件上传：

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet(name = "FileUploadServlet", urlPatterns = {"/upload"})
public class InsecureFileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream inputStream = request.getInputStream();
            // WARNING: Insecure file upload without proper validation and checks
            Files.copy(inputStream, Paths.get("uploads", "uploadedFile.txt"));
            response.getWriter().println("File uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}