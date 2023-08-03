package org.bouncycastle.pqc.crypto.gmss;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSVerify;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSignature;
public class GMSSKeyPairGenerator
    implements AsymmetricCipherKeyPairGenerator
{
    private GMSSRandom gmssRandom;
    private Digest messDigestTree;
    private byte[][] currentSeeds;
    private byte[][] nextNextSeeds;
    private byte[][] currentRootSigs;
    private GMSSDigestProvider digestProvider;
    private int mdLength;
    private int numLayer;
    private boolean initialized = false;
    private GMSSParameters gmssPS;
    private int[] heightOfTrees;
    private int[] otsIndex;
    private int[] K;
    private GMSSKeyGenerationParameters gmssParams;
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.3";
    public GMSSKeyPairGenerator(GMSSDigestProvider digestProvider)
    {
        this.digestProvider = digestProvider;
        messDigestTree = digestProvider.get();
        this.mdLength = messDigestTree.getDigestSize();
        this.gmssRandom = new GMSSRandom(messDigestTree);
    }
    private AsymmetricCipherKeyPair genKeyPair()
    {
        if (!initialized)
        {
            initializeDefault();
        }
        byte[][][] currentAuthPaths = new byte[numLayer][][];
        byte[][][] nextAuthPaths = new byte[numLayer - 1][][];
        Treehash[][] currentTreehash = new Treehash[numLayer][];
        Treehash[][] nextTreehash = new Treehash[numLayer - 1][];
        Vector[] currentStack = new Vector[numLayer];
        Vector[] nextStack = new Vector[numLayer - 1];
        Vector[][] currentRetain = new Vector[numLayer][];
        Vector[][] nextRetain = new Vector[numLayer - 1][];
        for (int i = 0; i < numLayer; i++)
        {
            currentAuthPaths[i] = new byte[heightOfTrees[i]][mdLength];
            currentTreehash[i] = new Treehash[heightOfTrees[i] - K[i]];
            if (i > 0)
            {
                nextAuthPaths[i - 1] = new byte[heightOfTrees[i]][mdLength];
                nextTreehash[i - 1] = new Treehash[heightOfTrees[i] - K[i]];
            }
            currentStack[i] = new Vector();
            if (i > 0)
            {
                nextStack[i - 1] = new Vector();
            }
        }
        byte[][] currentRoots = new byte[numLayer][mdLength];
        byte[][] nextRoots = new byte[numLayer - 1][mdLength];
        byte[][] seeds = new byte[numLayer][mdLength];
        for (int i = 0; i < numLayer; i++)
        {
            System.arraycopy(currentSeeds[i], 0, seeds[i], 0, mdLength);
        }
        currentRootSigs = new byte[numLayer - 1][mdLength];
        for (int h = numLayer - 1; h >= 0; h--)
        {
            GMSSRootCalc tree = new GMSSRootCalc(this.heightOfTrees[h], this.K[h], digestProvider);
            try
            {
                if (h == numLayer - 1)
                {
                    tree = this.generateCurrentAuthpathAndRoot(null, currentStack[h], seeds[h], h);
                }
                else
                {
                    tree = this.generateCurrentAuthpathAndRoot(currentRoots[h + 1], currentStack[h], seeds[h], h);
                }
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
            for (int i = 0; i < heightOfTrees[h]; i++)
            {
                System.arraycopy(tree.getAuthPath()[i], 0, currentAuthPaths[h][i], 0, mdLength);
            }
            currentRetain[h] = tree.getRetain();
            currentTreehash[h] = tree.getTreehash();
            System.arraycopy(tree.getRoot(), 0, currentRoots[h], 0, mdLength);
        }
        for (int h = numLayer - 2; h >= 0; h--)
        {
            GMSSRootCalc tree = this.generateNextAuthpathAndRoot(nextStack[h], seeds[h + 1], h + 1);
            for (int i = 0; i < heightOfTrees[h + 1]; i++)
            {
                System.arraycopy(tree.getAuthPath()[i], 0, nextAuthPaths[h][i], 0, mdLength);
            }
            nextRetain[h] = tree.getRetain();
            nextTreehash[h] = tree.getTreehash();
            System.arraycopy(tree.getRoot(), 0, nextRoots[h], 0, mdLength);
            System.arraycopy(seeds[h + 1], 0, this.nextNextSeeds[h], 0, mdLength);
        }
        GMSSPublicKeyParameters publicKey = new GMSSPublicKeyParameters(currentRoots[0], gmssPS);
        GMSSPrivateKeyParameters privateKey = new GMSSPrivateKeyParameters(currentSeeds, nextNextSeeds, currentAuthPaths,
            nextAuthPaths, currentTreehash, nextTreehash, currentStack, nextStack, currentRetain, nextRetain, nextRoots, currentRootSigs, gmssPS, digestProvider);
        return (new AsymmetricCipherKeyPair(publicKey, privateKey));
    }
    private GMSSRootCalc generateCurrentAuthpathAndRoot(byte[] lowerRoot, Vector currentStack, byte[] seed, int h)
    {
        byte[] help = new byte[mdLength];
        byte[] OTSseed = new byte[mdLength];
        OTSseed = gmssRandom.nextSeed(seed);
        WinternitzOTSignature ots;
        GMSSRootCalc treeToConstruct = new GMSSRootCalc(this.heightOfTrees[h], this.K[h], digestProvider);
        treeToConstruct.initialize(currentStack);
        if (h == numLayer - 1)
        {
            ots = new WinternitzOTSignature(OTSseed, digestProvider.get(), otsIndex[h]);
            help = ots.getPublicKey();
        }
        else
        {
            ots = new WinternitzOTSignature(OTSseed, digestProvider.get(), otsIndex[h]);
            currentRootSigs[h] = ots.getSignature(lowerRoot);
            WinternitzOTSVerify otsver = new WinternitzOTSVerify(digestProvider.get(), otsIndex[h]);
            help = otsver.Verify(lowerRoot, currentRootSigs[h]);
        }
        treeToConstruct.update(help);
        int seedForTreehashIndex = 3;
        int count = 0;
        for (int i = 1; i < (1 << this.heightOfTrees[h]); i++)
        {
            if (i == seedForTreehashIndex && count < this.heightOfTrees[h] - this.K[h])
            {
                treeToConstruct.initializeTreehashSeed(seed, count);
                seedForTreehashIndex *= 2;
                count++;
            }
            OTSseed = gmssRandom.nextSeed(seed);
            ots = new WinternitzOTSignature(OTSseed, digestProvider.get(), otsIndex[h]);
            treeToConstruct.update(ots.getPublicKey());
        }
        if (treeToConstruct.wasFinished())
        {
            return treeToConstruct;
        }
        System.err.println("Baum noch nicht fertig konstruiert!!!");
        return null;
    }
    private GMSSRootCalc generateNextAuthpathAndRoot(Vector nextStack, byte[] seed, int h)
    {
        byte[] OTSseed = new byte[numLayer];
        WinternitzOTSignature ots;
        GMSSRootCalc treeToConstruct = new GMSSRootCalc(this.heightOfTrees[h], this.K[h], this.digestProvider);
        treeToConstruct.initialize(nextStack);
        int seedForTreehashIndex = 3;
        int count = 0;
        for (int i = 0; i < (1 << this.heightOfTrees[h]); i++)
        {
            if (i == seedForTreehashIndex && count < this.heightOfTrees[h] - this.K[h])
            {
                treeToConstruct.initializeTreehashSeed(seed, count);
                seedForTreehashIndex *= 2;
                count++;
            }
            OTSseed = gmssRandom.nextSeed(seed);
            ots = new WinternitzOTSignature(OTSseed, digestProvider.get(), otsIndex[h]);
            treeToConstruct.update(ots.getPublicKey());
        }
        if (treeToConstruct.wasFinished())
        {
            return treeToConstruct;
        }
        System.err.println("N�chster Baum noch nicht fertig konstruiert!!!");
        return null;
    }
    public void initialize(int keySize, SecureRandom secureRandom)
    {
        KeyGenerationParameters kgp;
        if (keySize <= 10)
        { 
            int[] defh = {10};
            int[] defw = {3};
            int[] defk = {2};
            kgp = new GMSSKeyGenerationParameters(secureRandom, new GMSSParameters(defh.length, defh, defw, defk));
        }
        else if (keySize <= 20)
        { 
            int[] defh = {10, 10};
            int[] defw = {5, 4};
            int[] defk = {2, 2};
            kgp = new GMSSKeyGenerationParameters(secureRandom, new GMSSParameters(defh.length, defh, defw, defk));
        }
        else
        { 
            int[] defh = {10, 10, 10, 10};
            int[] defw = {9, 9, 9, 3};
            int[] defk = {2, 2, 2, 2};
            kgp = new GMSSKeyGenerationParameters(secureRandom, new GMSSParameters(defh.length, defh, defw, defk));
        }
        this.initialize(kgp);
    }
    public void initialize(KeyGenerationParameters param)
    {
        this.gmssParams = (GMSSKeyGenerationParameters)param;
        this.gmssPS = new GMSSParameters(gmssParams.getParameters().getNumOfLayers(), gmssParams.getParameters().getHeightOfTrees(),
            gmssParams.getParameters().getWinternitzParameter(), gmssParams.getParameters().getK());
        this.numLayer = gmssPS.getNumOfLayers();
        this.heightOfTrees = gmssPS.getHeightOfTrees();
        this.otsIndex = gmssPS.getWinternitzParameter();
        this.K = gmssPS.getK();
        this.currentSeeds = new byte[numLayer][mdLength];
        this.nextNextSeeds = new byte[numLayer - 1][mdLength];
        SecureRandom secRan = new SecureRandom();
        for (int i = 0; i < numLayer; i++)
        {
            secRan.nextBytes(currentSeeds[i]);
            gmssRandom.nextSeed(currentSeeds[i]);
        }
        this.initialized = true;
    }
    private void initializeDefault()
    {
        int[] defh = {10, 10, 10, 10};
        int[] defw = {3, 3, 3, 3};
        int[] defk = {2, 2, 2, 2};
        KeyGenerationParameters kgp = new GMSSKeyGenerationParameters(new SecureRandom(), new GMSSParameters(defh.length, defh, defw, defk));
        this.initialize(kgp);
    }
    public void init(KeyGenerationParameters param)
    {
        this.initialize(param);
    }
    public AsymmetricCipherKeyPair generateKeyPair()
    {
        return genKeyPair();
    }
}
