
package org.gradle.plugins.signing.signatory.pgp;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.gradle.api.UncheckedIOException;
import org.gradle.internal.UncheckedException;
import org.gradle.plugins.signing.signatory.SignatorySupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
public class PgpSignatory extends SignatorySupport {
    {
        Security.addProvider(new BouncyCastleProvider());
    }
    private final String name;
    private final PGPSecretKey secretKey;
    private final PGPPrivateKey privateKey;
    public PgpSignatory(String name, PGPSecretKey secretKey, String password) {
        this.name = name;
        this.secretKey = secretKey;
        this.privateKey = createPrivateKey(secretKey, password);
    }
    @Override
    public final String getName() {
        return name;
    }
    @Override
    public void sign(InputStream toSign, OutputStream signatureDestination) {
        PGPSignatureGenerator generator = createSignatureGenerator();
        try {
            feedGeneratorWith(toSign, generator);
            PGPSignature signature = generator.generate();
            writeSignatureTo(signatureDestination, signature);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (PGPException e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }
    }
    @Override
    public String getKeyId() {
        PgpKeyId id = new PgpKeyId(secretKey.getKeyID());
        return id == null ? null : id.getAsHex();
    }
    private void feedGeneratorWith(InputStream toSign, PGPSignatureGenerator generator) throws IOException {
        byte[] buffer = new byte[1024];
        int read = toSign.read(buffer);
        while (read > 0) {
            generator.update(buffer, 0, read);
            read = toSign.read(buffer);
        }
    }
    private void writeSignatureTo(OutputStream signatureDestination, PGPSignature pgpSignature) throws PGPException, IOException {
        BCPGOutputStream bufferedOutput = new BCPGOutputStream(signatureDestination);
        pgpSignature.encode(bufferedOutput);
        bufferedOutput.flush();
    }
    public PGPSignatureGenerator createSignatureGenerator() {
        try {
            PGPSignatureGenerator generator = new PGPSignatureGenerator(new BcPGPContentSignerBuilder(secretKey.getPublicKey().getAlgorithm(), PGPUtil.SHA1));
            generator.init(PGPSignature.BINARY_DOCUMENT, privateKey);
            return generator;
        } catch (PGPException e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }
    }
    private PGPPrivateKey createPrivateKey(PGPSecretKey secretKey, String password) {
        try {
            PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(password.toCharArray());
            return secretKey.extractPrivateKey(decryptor);
        } catch (PGPException e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }
    }
}
