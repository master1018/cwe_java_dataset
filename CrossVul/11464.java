
package org.opencastproject.kernel.security;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
public class SystemTokenBasedRememberMeService extends TokenBasedRememberMeServices {
  private Logger logger = LoggerFactory.getLogger(SystemTokenBasedRememberMeService.class);
  private String key;
  @Deprecated
  public SystemTokenBasedRememberMeService() {
    super();
    setKey(null);
  }
  public SystemTokenBasedRememberMeService(String key, UserDetailsService userDetailsService) {
    super(key, userDetailsService);
    setKey(key);
  }
  @Override
  public void setKey(String key) {
    StringBuilder keyBuilder = new StringBuilder(Objects.toString(key, ""));
    try {
      keyBuilder.append(InetAddress.getLocalHost());
    } catch (UnknownHostException e) {
    }
    for (String procFile: Arrays.asList("/proc/version", "/proc/partitions")) {
      try (FileInputStream fileInputStream = new FileInputStream(new File(procFile))) {
        keyBuilder.append(IOUtils.toString(fileInputStream, StandardCharsets.UTF_8));
      } catch (IOException e) {
      }
    }
    key = keyBuilder.toString();
    if (key.isEmpty()) {
      logger.warn("Could not generate semi-persistent remember-me key. Will generate a non-persistent random one.");
      key = Double.toString(Math.random());
    }
    logger.debug("Remember me key before hashing: {}", key);
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      key = new String(Hex.encode(digest.digest(key.getBytes())));
    } catch (NoSuchAlgorithmException e) {
      logger.warn("No SHA-512 algorithm available!");
    }
    logger.debug("Calculated remember me key: {}", key);
    this.key = key;
    super.setKey(key);
  }
  @Override
  public String getKey() {
    return this.key;
  }
  @Override
  protected String makeTokenSignature(long tokenExpiryTime, String username, String password) {
    String data = username + ":" + tokenExpiryTime + ":" + password + ":" + getKey();
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("No SHA-512 algorithm available!");
    }
    return new String(Hex.encode(digest.digest(data.getBytes())));
  }
}
