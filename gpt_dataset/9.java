public class InsecureEnvironmentExecution {
    public static void main(String[] args) {
        String directoryPath = System.getenv("MY_DIRECTORY"); // Vulnerable to CWE-15

        // Concatenating user-controlled data (environment variable) with a command
        String command = "ls " + directoryPath;

        try {
            Process process = Runtime.getRuntime().exec(command);
            // Process the output of the command here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

