package org.bouncycastle.crypto.agreement;
import java.math.BigInteger;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
public class DHBasicAgreement
    implements BasicAgreement
{
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private DHPrivateKeyParameters  key;
    private DHParameters            dhParams;
    public void init(
        CipherParameters    param)
    {
        AsymmetricKeyParameter  kParam;
        if (param instanceof ParametersWithRandom)
        {
            ParametersWithRandom rParam = (ParametersWithRandom)param;
            kParam = (AsymmetricKeyParameter)rParam.getParameters();
        }
        else
        {
            kParam = (AsymmetricKeyParameter)param;
        }
        if (!(kParam instanceof DHPrivateKeyParameters))
        {
            throw new IllegalArgumentException("DHEngine expects DHPrivateKeyParameters");
        }
        this.key = (DHPrivateKeyParameters)kParam;
        this.dhParams = key.getParameters();
    }
    public int getFieldSize()
    {
        return (key.getParameters().getP().bitLength() + 7) / 8;
    }
    public BigInteger calculateAgreement(
        CipherParameters   pubKey)
    {
        DHPublicKeyParameters   pub = (DHPublicKeyParameters)pubKey;
        if (!pub.getParameters().equals(dhParams))
        {
            throw new IllegalArgumentException("Diffie-Hellman public key has wrong parameters.");
        }
        BigInteger result = pub.getY().modPow(key.getX(), dhParams.getP());
        if (result.compareTo(ONE) == 0)
        {
            throw new IllegalStateException("Shared key can't be 1");
        }
        return result;
    }
}
