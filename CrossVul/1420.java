package org.bouncycastle.tls.crypto.impl.jcajce;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import javax.crypto.Cipher;
import org.bouncycastle.tls.Certificate;
import org.bouncycastle.tls.ProtocolVersion;
import org.bouncycastle.tls.TlsCredentialedDecryptor;
import org.bouncycastle.tls.crypto.TlsCryptoParameters;
import org.bouncycastle.tls.crypto.TlsSecret;
import org.bouncycastle.util.Arrays;
public class JceDefaultTlsCredentialedDecryptor
    implements TlsCredentialedDecryptor
{
    protected JcaTlsCrypto crypto;
    protected Certificate certificate;
    protected PrivateKey privateKey;
    public JceDefaultTlsCredentialedDecryptor(JcaTlsCrypto crypto, Certificate certificate,
                                              PrivateKey privateKey)
    {
        if (crypto == null)
        {
            throw new IllegalArgumentException("'crypto' cannot be null");
        }
        if (certificate == null)
        {
            throw new IllegalArgumentException("'certificate' cannot be null");
        }
        if (certificate.isEmpty())
        {
            throw new IllegalArgumentException("'certificate' cannot be empty");
        }
        if (privateKey == null)
        {
            throw new IllegalArgumentException("'privateKey' cannot be null");
        }
        if (privateKey instanceof RSAPrivateKey || "RSA".equals(privateKey.getAlgorithm()))
        {
            this.crypto = crypto;
            this.certificate = certificate;
            this.privateKey = privateKey;
        }
        else
        {
            throw new IllegalArgumentException("'privateKey' type not supported: "
                + privateKey.getClass().getName());
        }
    }
    public Certificate getCertificate()
    {
        return certificate;
    }
    public TlsSecret decrypt(TlsCryptoParameters cryptoParams, byte[] ciphertext) throws IOException
    {
        return safeDecryptPreMasterSecret(cryptoParams, privateKey, ciphertext);
    }
    protected TlsSecret safeDecryptPreMasterSecret(TlsCryptoParameters cryptoParams, PrivateKey rsaServerPrivateKey,
                                                   byte[] encryptedPreMasterSecret)
    {
        SecureRandom secureRandom = crypto.getSecureRandom();
        ProtocolVersion clientVersion = cryptoParams.getClientVersion();
        boolean versionNumberCheckDisabled = false;
        byte[] fallback = new byte[48];
        secureRandom.nextBytes(fallback);
        byte[] M = Arrays.clone(fallback);
        try
        {
            Cipher c = crypto.createRSAEncryptionCipher();
            c.init(Cipher.DECRYPT_MODE, rsaServerPrivateKey);
            M = c.doFinal(encryptedPreMasterSecret);
        }
        catch (Exception e)
        {
        }
        if (versionNumberCheckDisabled && clientVersion.isEqualOrEarlierVersionOf(ProtocolVersion.TLSv10))
        {
        }
        else
        {
            int correct = (clientVersion.getMajorVersion() ^ (M[0] & 0xff))
                | (clientVersion.getMinorVersion() ^ (M[1] & 0xff));
            correct |= correct >> 1;
            correct |= correct >> 2;
            correct |= correct >> 4;
            int mask = ~((correct & 1) - 1);
            for (int i = 0; i < 48; i++)
            {
                M[i] = (byte)((M[i] & (~mask)) | (fallback[i] & mask));
            }
        }
        return crypto.createSecret(M);
    }
}
