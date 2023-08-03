
package hudson.util;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.trilead.ssh2.crypto.Base64;
import jenkins.model.Jenkins;
import hudson.Util;
import jenkins.security.CryptoConfidentialKey;
import org.kohsuke.stapler.Stapler;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Pattern;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
public final class Secret implements Serializable {
    private final String value;
    private Secret(String value) {
        this.value = value;
    }
    @Override
    @Deprecated
    public String toString() {
        return value;
    }
    public String getPlainText() {
        return value;
    }
    @Override
    public boolean equals(Object that) {
        return that instanceof Secret && value.equals(((Secret)that).value);
    }
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    @Deprecated
     static SecretKey getLegacyKey() throws GeneralSecurityException {
        String secret = SECRET;
        if(secret==null)    return Jenkins.getInstance().getSecretKeyAsAES128();
        return Util.toAes128Key(secret);
    }
    public String getEncryptedValue() {
        try {
            Cipher cipher = KEY.encrypt();
            return new String(Base64.encode(cipher.doFinal((value+MAGIC).getBytes("UTF-8"))));
        } catch (GeneralSecurityException e) {
            throw new Error(e); 
        } catch (UnsupportedEncodingException e) {
            throw new Error(e); 
        }
    }
    @Restricted(NoExternalUse.class)
    public static final Pattern ENCRYPTED_VALUE_PATTERN = Pattern.compile("[A-Za-z0-9+/]+={0,2}");
    public static Secret decrypt(String data) {
        if(data==null)      return null;
        try {
            byte[] in = Base64.decode(data.toCharArray());
            Secret s = tryDecrypt(KEY.decrypt(), in);
            if (s!=null)    return s;
            Cipher cipher = getCipher("AES");
            cipher.init(Cipher.DECRYPT_MODE, getLegacyKey());
            return tryDecrypt(cipher, in);
        } catch (GeneralSecurityException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            throw new Error(e); 
        } catch (IOException e) {
            return null;
        }
    }
     static Secret tryDecrypt(Cipher cipher, byte[] in) throws UnsupportedEncodingException {
        try {
            String plainText = new String(cipher.doFinal(in), "UTF-8");
            if(plainText.endsWith(MAGIC))
                return new Secret(plainText.substring(0,plainText.length()-MAGIC.length()));
            return null;
        } catch (GeneralSecurityException e) {
            return null; 
        }
    }
    public static Cipher getCipher(String algorithm) throws GeneralSecurityException {
        return PROVIDER != null ? Cipher.getInstance(algorithm, PROVIDER)
                                : Cipher.getInstance(algorithm);
    }
    public static Secret fromString(String data) {
        data = Util.fixNull(data);
        Secret s = decrypt(data);
        if(s==null) s=new Secret(data);
        return s;
    }
    public static String toString(Secret s) {
        return s==null ? "" : s.value;
    }
    public static final class ConverterImpl implements Converter {
        public ConverterImpl() {
        }
        public boolean canConvert(Class type) {
            return type==Secret.class;
        }
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            Secret src = (Secret) source;
            writer.setValue(src.getEncryptedValue());
        }
        public Object unmarshal(HierarchicalStreamReader reader, final UnmarshallingContext context) {
            return fromString(reader.getValue());
        }
    }
    private static final String MAGIC = "::::MAGIC::::";
    private static final String PROVIDER = System.getProperty(Secret.class.getName()+".provider");
     static String SECRET = null;
    private static final CryptoConfidentialKey KEY = new CryptoConfidentialKey(Secret.class.getName());
    private static final long serialVersionUID = 1L;
    static {
        Stapler.CONVERT_UTILS.register(new org.apache.commons.beanutils.Converter() {
            public Secret convert(Class type, Object value) {
                return Secret.fromString(value.toString());
            }
        }, Secret.class);
    }
}
