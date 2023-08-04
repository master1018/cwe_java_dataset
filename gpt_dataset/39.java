//4. 不安全的密码存储：

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InsecurePasswordStorageExample {
    public static void main(String[] args) {
        String password = "examplePassword";

        // WARNING: Insecure password storage using MD5 (weak hashing algorithm)
        String hashedPassword = md5Hash(password);
        System.out.println("Hashed Password: " + hashedPassword);
    }

    public static String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}