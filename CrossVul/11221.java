
package hudson.util;
import com.trilead.ssh2.crypto.Base64;
import hudson.Util;
import jenkins.model.Jenkins;
import jenkins.security.CryptoConfidentialKey;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import static java.nio.charset.StandardCharsets.UTF_8;
@Restricted(NoExternalUse.class)
public class HistoricalSecrets {
     static Secret decrypt(String data, CryptoConfidentialKey key) throws IOException, GeneralSecurityException {
        byte[] in = Base64.decode(data.toCharArray());
        Secret s = tryDecrypt(key.decrypt(), in);
        if (s!=null)    return s;
        Cipher cipher = Secret.getCipher("AES");
        cipher.init(Cipher.DECRYPT_MODE, getLegacyKey());
        return tryDecrypt(cipher, in);
    }
     static Secret tryDecrypt(Cipher cipher, byte[] in) {
        try {
            String plainText = new String(cipher.doFinal(in), UTF_8);
            if(plainText.endsWith(MAGIC))
                return new Secret(plainText.substring(0,plainText.length()-MAGIC.length()));
            return null;
        } catch (GeneralSecurityException e) {
            return null; 
        }
    }
    @Deprecated
     static SecretKey getLegacyKey() throws GeneralSecurityException {
        String secret = Secret.SECRET;
        if(secret==null)    return Jenkins.getInstance().getSecretKeyAsAES128();
        return Util.toAes128Key(secret);
    }
    private static final String MAGIC = "::::MAGIC::::";
}
