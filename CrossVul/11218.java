
package hudson.util;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.trilead.ssh2.crypto.Base64;
import java.util.Arrays;
import jenkins.model.Jenkins;
import hudson.Util;
import jenkins.security.CryptoConfidentialKey;
import org.kohsuke.stapler.Stapler;
import javax.crypto.Cipher;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Pattern;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import static java.nio.charset.StandardCharsets.UTF_8;
public final class Secret implements Serializable {
    private static final byte PAYLOAD_V1 = 1;
    private final String value;
    private byte[] iv;
     Secret(String value) {
        this.value = value;
    }
     Secret(String value, byte[] iv) {
        this.value = value;
        this.iv = iv;
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
    public String getEncryptedValue() {
        try {
            synchronized (this) {
                if (iv == null) { 
                    iv = KEY.newIv();
                }
            }
            Cipher cipher = KEY.encrypt(iv);
            byte[] encrypted = cipher.doFinal(this.value.getBytes(UTF_8));
            byte[] payload = new byte[1 + 8 + iv.length + encrypted.length];
            int pos = 0;
            payload[pos++] = PAYLOAD_V1;
            payload[pos++] = (byte)(iv.length >> 24);
            payload[pos++] = (byte)(iv.length >> 16);
            payload[pos++] = (byte)(iv.length >> 8);
            payload[pos++] = (byte)(iv.length);
            payload[pos++] = (byte)(encrypted.length >> 24);
            payload[pos++] = (byte)(encrypted.length >> 16);
            payload[pos++] = (byte)(encrypted.length >> 8);
            payload[pos++] = (byte)(encrypted.length);
            System.arraycopy(iv, 0, payload, pos, iv.length);
            pos+=iv.length;
            System.arraycopy(encrypted, 0, payload, pos, encrypted.length);
            return "{"+new String(Base64.encode(payload))+"}";
        } catch (GeneralSecurityException e) {
            throw new Error(e); 
        }
    }
    @Restricted(NoExternalUse.class)
    public static final Pattern ENCRYPTED_VALUE_PATTERN = Pattern.compile("\\{?[A-Za-z0-9+/]+={0,2}}?");
    public static Secret decrypt(String data) {
        if (data == null) return null;
        if (data.startsWith("{") && data.endsWith("}")) { 
            byte[] payload;
            try {
                payload = Base64.decode(data.substring(1, data.length()-1).toCharArray());
            } catch (IOException e) {
                return null;
            }
            switch (payload[0]) {
                case PAYLOAD_V1:
                    int ivLength = ((payload[1] & 0xff) << 24)
                            | ((payload[2] & 0xff) << 16)
                            | ((payload[3] & 0xff) << 8)
                            | (payload[4] & 0xff);
                    int dataLength = ((payload[5] & 0xff) << 24)
                            | ((payload[6] & 0xff) << 16)
                            | ((payload[7] & 0xff) << 8)
                            | (payload[8] & 0xff);
                    if (payload.length != 1 + 8 + ivLength + dataLength) {
                        return null;
                    }
                    byte[] iv = Arrays.copyOfRange(payload, 9, 9 + ivLength);
                    byte[] code = Arrays.copyOfRange(payload, 9+ivLength, payload.length);
                    String text;
                    try {
                        text = new String(KEY.decrypt(iv).doFinal(code), UTF_8);
                    } catch (GeneralSecurityException e) {
                        return null;
                    }
                    return new Secret(text, iv);
                default:
                    return null;
            }
        } else {
            try {
                return HistoricalSecrets.decrypt(data, KEY);
            } catch (GeneralSecurityException e) {
                return null;
            } catch (UnsupportedEncodingException e) {
                throw new Error(e); 
            } catch (IOException e) {
                return null;
            }
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
    private static final String PROVIDER = System.getProperty(Secret.class.getName()+".provider");
     static String SECRET = null;
    private static final CryptoConfidentialKey KEY = new CryptoConfidentialKey(Secret.class.getName());
    @Restricted(NoExternalUse.class)
     static void resetKeyForTest() {
        KEY.resetForTest();
    }
    private static final long serialVersionUID = 1L;
    static {
        Stapler.CONVERT_UTILS.register(new org.apache.commons.beanutils.Converter() {
            public Secret convert(Class type, Object value) {
                return Secret.fromString(value.toString());
            }
        }, Secret.class);
    }
}
