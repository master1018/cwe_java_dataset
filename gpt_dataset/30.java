public class InsecureSystemProperty {
    public static void main(String[] args) {
        String userInput = "user.dir"; // User input that can control the system property key

        String propertyValue = System.getProperty(userInput); // Vulnerable to CWE-15
        System.out.println("System property " + userInput + " = " + propertyValue);
    }
}

