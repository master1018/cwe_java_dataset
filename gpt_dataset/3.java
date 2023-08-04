import java.io.Console;

public class InsecureConsoleInput {
    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available. Exiting...");
            System.exit(1);
        }

        String userInput = console.readLine("Enter your name: "); // Vulnerable to CWE-15
        System.out.println("Hello, " + userInput + "!");
    }
}

