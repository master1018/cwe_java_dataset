import java.io.*;
import java.util.logging.Level;

public class PathTraversalExample {

    public static void main(String[] args) {
        String data = "../../../../etc/passwd"; // Relative path data, simulating the data received from the TCP connection

        String root;
        if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            /* running on Windows */
            root = "C:\\uploads\\";
        } else {
            /* running on non-Windows */
            root = "/home/user/uploads/";
        }

        if (data != null) {
            File file = new File(root + data);
            try (FileInputStream streamFileInputSink = new FileInputStream(file);
                 InputStreamReader readerInputStreamSink = new InputStreamReader(streamFileInputSink, "UTF-8");
                 BufferedReader readerBufferedSink = new BufferedReader(readerInputStreamSink)) {

                String line;
                while ((line = readerBufferedSink.readLine()) != null) {
                    System.out.println(line); // Output the contents of the file to the console
                }

            } catch (IOException exceptIO) {
                // Handle any exceptions here
                System.err.println("Error with stream reading: " + exceptIO.getMessage());
            }
        }
    }
}

