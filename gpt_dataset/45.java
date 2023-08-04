//10. 不安全的随机数生成：

import java.util.Random;

public class InsecureRandomExample {
    public static void main(String[] args) {
        Random random = new Random();
        int insecureRandomNumber = random.nextInt(100);

        System.out.println("Insecure Random Number: " + insecureRandomNumber);
    }
}