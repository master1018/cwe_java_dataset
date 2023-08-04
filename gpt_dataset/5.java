import java.io.Console;

public class InsecureConsoleInput {
    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available. Exiting...");
            System.exit(1);
        }

        String input = console.readLine("Enter the command to execute: "); // Vulnerable to CWE-15
        System.out.println("Executing command: " + input);
    }
}

