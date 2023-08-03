
package org.opencastproject.kernel.security;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
public class CustomPasswordEncoder implements PasswordEncoder {
  private Logger logger = LoggerFactory.getLogger(CustomPasswordEncoder.class);
  public String encodePassword(final String rawPassword) {
    return encodePassword(rawPassword, null);
  }
  @Override
  public String encodePassword(final String rawPassword, final Object ignored) {
    return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
  }
  @Override
  public boolean isPasswordValid(String encodedPassword, String rawPassword, Object salt) {
    if (encodedPassword.length() == 32) {
      final String hash = md5Encode(rawPassword, salt);
      logger.debug("Checking md5 hashed password '{}' against encoded password '{}'", hash, encodedPassword);
      return hash.equals(encodedPassword);
    }
    logger.debug("Verifying BCrypt hash {}", encodedPassword);
    return BCrypt.checkpw(rawPassword, encodedPassword);
  }
  public static String md5Encode(String clearText, Object salt) throws IllegalArgumentException {
    if (clearText == null || salt == null)
      throw new IllegalArgumentException("clearText and salt must not be null");
    return DigestUtils.md5Hex(clearText + "{" + salt.toString() + "}");
  }
}
