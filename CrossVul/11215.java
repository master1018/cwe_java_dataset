package org.bouncycastle.jcajce.provider.asymmetric.dsa;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.util.Hashtable;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.DSAKeyPairGenerator;
import org.bouncycastle.crypto.generators.DSAParametersGenerator;
import org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameterGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Properties;
public class KeyPairGeneratorSpi
    extends java.security.KeyPairGenerator
{
    private static Hashtable params = new Hashtable();
    private static Object    lock = new Object();
    DSAKeyGenerationParameters param;
    DSAKeyPairGenerator engine = new DSAKeyPairGenerator();
    int strength = 1024;
    int certainty = 20;
    SecureRandom random = new SecureRandom();
    boolean initialised = false;
    public KeyPairGeneratorSpi()
    {
        super("DSA");
    }
    public void initialize(
        int strength,
        SecureRandom random)
    {
        if (strength < 512 || strength > 4096 || ((strength < 1024) && strength % 64 != 0) || (strength >= 1024 && strength % 1024 != 0))
        {
            throw new InvalidParameterException("strength must be from 512 - 4096 and a multiple of 1024 above 1024");
        }
        this.strength = strength;
        this.random = random;
        this.initialised = false;
    }
    public void initialize(
        AlgorithmParameterSpec params,
        SecureRandom random)
        throws InvalidAlgorithmParameterException
    {
        if (!(params instanceof DSAParameterSpec))
        {
            throw new InvalidAlgorithmParameterException("parameter object not a DSAParameterSpec");
        }
        DSAParameterSpec dsaParams = (DSAParameterSpec)params;
        param = new DSAKeyGenerationParameters(random, new DSAParameters(dsaParams.getP(), dsaParams.getQ(), dsaParams.getG()));
        engine.init(param);
        initialised = true;
    }
    public KeyPair generateKeyPair()
    {
        if (!initialised)
        {
            Integer paramStrength = Integers.valueOf(strength);
            if (params.containsKey(paramStrength))
            {
                param = (DSAKeyGenerationParameters)params.get(paramStrength);
            }
            else
            {
                synchronized (lock)
                {
                    if (params.containsKey(paramStrength))
                    {
                        param = (DSAKeyGenerationParameters)params.get(paramStrength);
                    }
                    else
                    {
                        DSAParametersGenerator pGen;
                        DSAParameterGenerationParameters dsaParams;
                        if (strength == 1024)
                        {
                            pGen = new DSAParametersGenerator();
                            if (Properties.isOverrideSet("org.bouncycastle.dsa.FIPS186-2for1024bits"))
                            {
                                pGen.init(strength, certainty, random);
                            }
                            else
                            {
                                dsaParams = new DSAParameterGenerationParameters(1024, 160, certainty, random);
                                pGen.init(dsaParams);
                            }
                        }
                        else if (strength > 1024)
                        {
                            dsaParams = new DSAParameterGenerationParameters(strength, 256, certainty, random);
                            pGen = new DSAParametersGenerator(new SHA256Digest());
                            pGen.init(dsaParams);
                        }
                        else
                        {
                            pGen = new DSAParametersGenerator();
                            pGen.init(strength, certainty, random);
                        }
                        param = new DSAKeyGenerationParameters(random, pGen.generateParameters());
                        params.put(paramStrength, param);
                    }
                }
            }
            engine.init(param);
            initialised = true;
        }
        AsymmetricCipherKeyPair pair = engine.generateKeyPair();
        DSAPublicKeyParameters pub = (DSAPublicKeyParameters)pair.getPublic();
        DSAPrivateKeyParameters priv = (DSAPrivateKeyParameters)pair.getPrivate();
        return new KeyPair(new BCDSAPublicKey(pub), new BCDSAPrivateKey(priv));
    }
}
