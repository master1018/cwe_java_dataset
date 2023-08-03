
package org.opencastproject.util;
import org.apache.commons.codec.digest.DigestUtils;
public final class PasswordEncoder {
  private PasswordEncoder() {
  }
  public static String encode(String clearText, Object salt) throws IllegalArgumentException {
    if (clearText == null || salt == null)
      throw new IllegalArgumentException("clearText and salt must not be null");
    return DigestUtils.md5Hex(clearText + "{" + salt.toString() + "}");
  }
}
