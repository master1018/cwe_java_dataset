package org.bouncycastle.pqc.crypto.xmss;
import java.io.IOException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;
public final class XMSSPrivateKeyParameters
    extends AsymmetricKeyParameter
    implements XMSSStoreableObjectInterface
{
    private final XMSSParameters params;
    private final byte[] secretKeySeed;
    private final byte[] secretKeyPRF;
    private final byte[] publicSeed;
    private final byte[] root;
    private final BDS bdsState;
    private XMSSPrivateKeyParameters(Builder builder)
    {
        super(true);
        params = builder.params;
        if (params == null)
        {
            throw new NullPointerException("params == null");
        }
        int n = params.getDigestSize();
        byte[] privateKey = builder.privateKey;
        if (privateKey != null)
        {
            if (builder.xmss == null)
            {
                throw new NullPointerException("xmss == null");
            }
            int height = params.getHeight();
            int indexSize = 4;
            int secretKeySize = n;
            int secretKeyPRFSize = n;
            int publicSeedSize = n;
            int rootSize = n;
            int position = 0;
            int index = Pack.bigEndianToInt(privateKey, position);
            if (!XMSSUtil.isIndexValid(height, index))
            {
                throw new IllegalArgumentException("index out of bounds");
            }
            position += indexSize;
            secretKeySeed = XMSSUtil.extractBytesAtOffset(privateKey, position, secretKeySize);
            position += secretKeySize;
            secretKeyPRF = XMSSUtil.extractBytesAtOffset(privateKey, position, secretKeyPRFSize);
            position += secretKeyPRFSize;
            publicSeed = XMSSUtil.extractBytesAtOffset(privateKey, position, publicSeedSize);
            position += publicSeedSize;
            root = XMSSUtil.extractBytesAtOffset(privateKey, position, rootSize);
            position += rootSize;
            byte[] bdsStateBinary = XMSSUtil.extractBytesAtOffset(privateKey, position, privateKey.length - position);
            try
            {
                BDS bdsImport = (BDS)XMSSUtil.deserialize(bdsStateBinary, BDS.class);
                bdsImport.setXMSS(builder.xmss);
                bdsImport.validate();
                if (bdsImport.getIndex() != index)
                {
                    throw new IllegalStateException("serialized BDS has wrong index");
                }
                bdsState = bdsImport;
            }
            catch (IOException e)
            {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
            catch (ClassNotFoundException e)
            {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
        else
        {
            byte[] tmpSecretKeySeed = builder.secretKeySeed;
            if (tmpSecretKeySeed != null)
            {
                if (tmpSecretKeySeed.length != n)
                {
                    throw new IllegalArgumentException("size of secretKeySeed needs to be equal size of digest");
                }
                secretKeySeed = tmpSecretKeySeed;
            }
            else
            {
                secretKeySeed = new byte[n];
            }
            byte[] tmpSecretKeyPRF = builder.secretKeyPRF;
            if (tmpSecretKeyPRF != null)
            {
                if (tmpSecretKeyPRF.length != n)
                {
                    throw new IllegalArgumentException("size of secretKeyPRF needs to be equal size of digest");
                }
                secretKeyPRF = tmpSecretKeyPRF;
            }
            else
            {
                secretKeyPRF = new byte[n];
            }
            byte[] tmpPublicSeed = builder.publicSeed;
            if (tmpPublicSeed != null)
            {
                if (tmpPublicSeed.length != n)
                {
                    throw new IllegalArgumentException("size of publicSeed needs to be equal size of digest");
                }
                publicSeed = tmpPublicSeed;
            }
            else
            {
                publicSeed = new byte[n];
            }
            byte[] tmpRoot = builder.root;
            if (tmpRoot != null)
            {
                if (tmpRoot.length != n)
                {
                    throw new IllegalArgumentException("size of root needs to be equal size of digest");
                }
                root = tmpRoot;
            }
            else
            {
                root = new byte[n];
            }
            BDS tmpBDSState = builder.bdsState;
            if (tmpBDSState != null)
            {
                bdsState = tmpBDSState;
            }
            else
            {
                if (builder.index < ((1 << params.getHeight()) - 2) && tmpPublicSeed != null && tmpSecretKeySeed != null)
                {
                    bdsState = new BDS(params, tmpPublicSeed, tmpSecretKeySeed, (OTSHashAddress)new OTSHashAddress.Builder().build(), builder.index);
                }
                else
                {
                    bdsState = new BDS(params, builder.index);
                }
            }
        }
    }
    public static class Builder
    {
        private final XMSSParameters params;
        private int index = 0;
        private byte[] secretKeySeed = null;
        private byte[] secretKeyPRF = null;
        private byte[] publicSeed = null;
        private byte[] root = null;
        private BDS bdsState = null;
        private byte[] privateKey = null;
        private XMSSParameters xmss = null;
        public Builder(XMSSParameters params)
        {
            super();
            this.params = params;
        }
        public Builder withIndex(int val)
        {
            index = val;
            return this;
        }
        public Builder withSecretKeySeed(byte[] val)
        {
            secretKeySeed = XMSSUtil.cloneArray(val);
            return this;
        }
        public Builder withSecretKeyPRF(byte[] val)
        {
            secretKeyPRF = XMSSUtil.cloneArray(val);
            return this;
        }
        public Builder withPublicSeed(byte[] val)
        {
            publicSeed = XMSSUtil.cloneArray(val);
            return this;
        }
        public Builder withRoot(byte[] val)
        {
            root = XMSSUtil.cloneArray(val);
            return this;
        }
        public Builder withBDSState(BDS valBDS)
        {
            bdsState = valBDS;
            return this;
        }
        public Builder withPrivateKey(byte[] privateKeyVal, XMSSParameters xmssParameters)
        {
            privateKey = XMSSUtil.cloneArray(privateKeyVal);
            xmss = xmssParameters;
            return this;
        }
        public XMSSPrivateKeyParameters build()
        {
            return new XMSSPrivateKeyParameters(this);
        }
    }
    public byte[] toByteArray()
    {
        int n = params.getDigestSize();
        int indexSize = 4;
        int secretKeySize = n;
        int secretKeyPRFSize = n;
        int publicSeedSize = n;
        int rootSize = n;
        int totalSize = indexSize + secretKeySize + secretKeyPRFSize + publicSeedSize + rootSize;
        byte[] out = new byte[totalSize];
        int position = 0;
        Pack.intToBigEndian(bdsState.getIndex(), out, position);
        position += indexSize;
        XMSSUtil.copyBytesAtOffset(out, secretKeySeed, position);
        position += secretKeySize;
        XMSSUtil.copyBytesAtOffset(out, secretKeyPRF, position);
        position += secretKeyPRFSize;
        XMSSUtil.copyBytesAtOffset(out, publicSeed, position);
        position += publicSeedSize;
        XMSSUtil.copyBytesAtOffset(out, root, position);
        byte[] bdsStateOut = null;
        try
        {
            bdsStateOut = XMSSUtil.serialize(bdsState);
        }
        catch (IOException e)
        {
            throw new RuntimeException("error serializing bds state: " + e.getMessage());
        }
        return Arrays.concatenate(out, bdsStateOut);
    }
    public int getIndex()
    {
        return bdsState.getIndex();
    }
    public byte[] getSecretKeySeed()
    {
        return XMSSUtil.cloneArray(secretKeySeed);
    }
    public byte[] getSecretKeyPRF()
    {
        return XMSSUtil.cloneArray(secretKeyPRF);
    }
    public byte[] getPublicSeed()
    {
        return XMSSUtil.cloneArray(publicSeed);
    }
    public byte[] getRoot()
    {
        return XMSSUtil.cloneArray(root);
    }
    BDS getBDSState()
    {
        return bdsState;
    }
    public XMSSParameters getParameters()
    {
        return params;
    }
    public XMSSPrivateKeyParameters getNextKey()
    {
        int treeHeight = this.params.getHeight();
        if (this.getIndex() < ((1 << treeHeight) - 1))
        {
            return new XMSSPrivateKeyParameters.Builder(params)
                .withSecretKeySeed(secretKeySeed).withSecretKeyPRF(secretKeyPRF)
                .withPublicSeed(publicSeed).withRoot(root)
                .withBDSState(bdsState.getNextState(publicSeed, secretKeySeed, (OTSHashAddress)new OTSHashAddress.Builder().build())).build();
        }
        else
        {
            return new XMSSPrivateKeyParameters.Builder(params)
                .withSecretKeySeed(secretKeySeed).withSecretKeyPRF(secretKeyPRF)
                .withPublicSeed(publicSeed).withRoot(root)
                .withBDSState(new BDS(params, getIndex() + 1)).build();  
        }
    }
}