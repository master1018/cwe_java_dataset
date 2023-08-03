package org.bouncycastle.crypto.generators;
import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.math.Primes;
import org.bouncycastle.math.ec.WNafUtil;
public class RSAKeyPairGenerator
    implements AsymmetricCipherKeyPairGenerator
{
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private RSAKeyGenerationParameters param;
    public void init(KeyGenerationParameters param)
    {
        this.param = (RSAKeyGenerationParameters)param;
    }
    public AsymmetricCipherKeyPair generateKeyPair()
    {
        AsymmetricCipherKeyPair result = null;
        boolean done = false;
        int strength = param.getStrength();
        int pbitlength = (strength + 1) / 2;
        int qbitlength = strength - pbitlength;
        int mindiffbits = (strength / 2) - 100;
        if (mindiffbits < strength / 3)
        {
            mindiffbits = strength / 3;
        }
        int minWeight = strength >> 2;
        BigInteger dLowerBound = BigInteger.valueOf(2).pow(strength / 2);
        BigInteger squaredBound = ONE.shiftLeft(strength - 1);
        BigInteger minDiff = ONE.shiftLeft(mindiffbits);
        while (!done)
        {
            BigInteger p, q, n, d, e, pSub1, qSub1, gcd, lcm;
            e = param.getPublicExponent();
            p = chooseRandomPrime(pbitlength, e, squaredBound);
            for (; ; )
            {
                q = chooseRandomPrime(qbitlength, e, squaredBound);
                BigInteger diff = q.subtract(p).abs();
                if (diff.bitLength() < mindiffbits || diff.compareTo(minDiff) <= 0)
                {
                    continue;
                }
                n = p.multiply(q);
                if (n.bitLength() != strength)
                {
                    p = p.max(q);
                    continue;
                }
                if (WNafUtil.getNafWeight(n) < minWeight)
                {
                    p = chooseRandomPrime(pbitlength, e, squaredBound);
                    continue;
                }
                break;
            }
            if (p.compareTo(q) < 0)
            {
                gcd = p;
                p = q;
                q = gcd;
            }
            pSub1 = p.subtract(ONE);
            qSub1 = q.subtract(ONE);
            gcd = pSub1.gcd(qSub1);
            lcm = pSub1.divide(gcd).multiply(qSub1);
            d = e.modInverse(lcm);
            if (d.compareTo(dLowerBound) <= 0)
            {
                continue;
            }
            else
            {
                done = true;
            }
            BigInteger dP, dQ, qInv;
            dP = d.remainder(pSub1);
            dQ = d.remainder(qSub1);
            qInv = q.modInverse(p);
            result = new AsymmetricCipherKeyPair(
                new RSAKeyParameters(false, n, e),
                new RSAPrivateCrtKeyParameters(n, e, d, p, q, dP, dQ, qInv));
        }
        return result;
    }
    protected BigInteger chooseRandomPrime(int bitlength, BigInteger e, BigInteger sqrdBound)
    {
        for (int i = 0; i != 5 * bitlength; i++)
        {
            BigInteger p = new BigInteger(bitlength, 1, param.getRandom());
            if (p.mod(e).equals(ONE))
            {
                continue;
            }
            if (p.multiply(p).compareTo(sqrdBound) < 0)
            {
                continue;
            }
            if (!isProbablePrime(p))
            {
                continue;
            }
            if (!e.gcd(p.subtract(ONE)).equals(ONE))
            {
                continue;
            }
            return p;
        }
        throw new IllegalStateException("unable to generate prime number for RSA key");
    }
    protected boolean isProbablePrime(BigInteger x)
    {
        int iterations = getNumberOfIterations(x.bitLength(), param.getCertainty());
        return !Primes.hasAnySmallFactors(x) && Primes.isMRProbablePrime(x, param.getRandom(), iterations);
    }
    private static int getNumberOfIterations(int bits, int certainty)
    {
        if (bits >= 1536)
        {
            return  certainty <= 100 ? 3
                :   certainty <= 128 ? 4
                :   4 + (certainty - 128 + 1) / 2;
        }
        else if (bits >= 1024)
        {
            return  certainty <= 100 ? 4
                :   certainty <= 112 ? 5
                :   5 + (certainty - 112 + 1) / 2;
        }
        else if (bits >= 512)
        {
            return  certainty <= 80  ? 5
                :   certainty <= 100 ? 7
                :   7 + (certainty - 100 + 1) / 2;
        }
        else
        {
            return  certainty <= 80  ? 40
                :   40 + (certainty - 80 + 1) / 2;
        }
    }
}
