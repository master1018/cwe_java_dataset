
package com.facebook.thrift.protocol;
import com.facebook.thrift.TException;
import com.facebook.thrift.transport.TTransport;
import java.nio.charset.StandardCharsets;
import java.util.Map;
public class TBinaryProtocol extends TProtocol {
  private static final TStruct ANONYMOUS_STRUCT = new TStruct();
  public static final int VERSION_MASK = 0xffff0000;
  public static final int VERSION_1 = 0x80010000;
  protected final boolean strictRead_;
  protected final boolean strictWrite_;
  protected int readLength_;
  protected boolean checkReadLength_;
  private final byte[] buffer = new byte[8];
  @SuppressWarnings("serial")
  public static class Factory implements TProtocolFactory {
    protected final boolean strictRead_;
    protected final boolean strictWrite_;
    protected int readLength_;
    public Factory() {
      this(false, true);
    }
    public Factory(boolean strictRead, boolean strictWrite) {
      this(strictRead, strictWrite, 0);
    }
    public Factory(boolean strictRead, boolean strictWrite, int readLength) {
      strictRead_ = strictRead;
      strictWrite_ = strictWrite;
      readLength_ = readLength;
    }
    public TProtocol getProtocol(TTransport trans) {
      TBinaryProtocol proto = new TBinaryProtocol(trans, strictRead_, strictWrite_);
      if (readLength_ != 0) {
        proto.setReadLength(readLength_);
      }
      return proto;
    }
  }
  public TBinaryProtocol(TTransport trans) {
    this(trans, false, true);
  }
  public TBinaryProtocol(TTransport trans, boolean strictRead, boolean strictWrite) {
    super(trans);
    strictRead_ = strictRead;
    strictWrite_ = strictWrite;
    checkReadLength_ = false;
  }
  public void writeMessageBegin(TMessage message) throws TException {
    if (message == null) {
      throw new TException("Can't write 'null' message");
    }
    if (strictWrite_) {
      int version = VERSION_1 | message.type;
      writeI32(version);
      writeString(message.name);
      writeI32(message.seqid);
    } else {
      writeString(message.name);
      writeByte(message.type);
      writeI32(message.seqid);
    }
  }
  public void writeMessageEnd() {}
  public void writeStructBegin(TStruct struct) {}
  public void writeStructEnd() {}
  public void writeFieldBegin(TField field) throws TException {
    writeByte(field.type);
    writeI16(field.id);
  }
  public void writeFieldEnd() {}
  public void writeFieldStop() throws TException {
    writeByte(TType.STOP);
  }
  public void writeMapBegin(TMap map) throws TException {
    writeByte(map.keyType);
    writeByte(map.valueType);
    writeI32(map.size);
  }
  public void writeMapEnd() {}
  public void writeListBegin(TList list) throws TException {
    writeByte(list.elemType);
    writeI32(list.size);
  }
  public void writeListEnd() {}
  public void writeSetBegin(TSet set) throws TException {
    writeByte(set.elemType);
    writeI32(set.size);
  }
  public void writeSetEnd() {}
  public void writeBool(boolean b) throws TException {
    writeByte(b ? (byte) 1 : (byte) 0);
  }
  public void writeByte(byte b) throws TException {
    buffer[0] = b;
    trans_.write(buffer, 0, 1);
  }
  public void writeI16(short i16) throws TException {
    buffer[0] = (byte) (0xff & (i16 >> 8));
    buffer[1] = (byte) (0xff & (i16));
    trans_.write(buffer, 0, 2);
  }
  public void writeI32(int i32) throws TException {
    buffer[0] = (byte) (0xff & (i32 >> 24));
    buffer[1] = (byte) (0xff & (i32 >> 16));
    buffer[2] = (byte) (0xff & (i32 >> 8));
    buffer[3] = (byte) (0xff & (i32));
    trans_.write(buffer, 0, 4);
  }
  public void writeI64(long i64) throws TException {
    buffer[0] = (byte) (0xff & (i64 >> 56));
    buffer[1] = (byte) (0xff & (i64 >> 48));
    buffer[2] = (byte) (0xff & (i64 >> 40));
    buffer[3] = (byte) (0xff & (i64 >> 32));
    buffer[4] = (byte) (0xff & (i64 >> 24));
    buffer[5] = (byte) (0xff & (i64 >> 16));
    buffer[6] = (byte) (0xff & (i64 >> 8));
    buffer[7] = (byte) (0xff & (i64));
    trans_.write(buffer, 0, 8);
  }
  public void writeDouble(double dub) throws TException {
    writeI64(Double.doubleToLongBits(dub));
  }
  public void writeFloat(float flt) throws TException {
    writeI32(Float.floatToIntBits(flt));
  }
  public void writeString(String str) throws TException {
    byte[] dat = str.getBytes(StandardCharsets.UTF_8);
    writeI32(dat.length);
    trans_.write(dat, 0, dat.length);
  }
  public void writeBinary(byte[] bin) throws TException {
    writeI32(bin.length);
    trans_.write(bin, 0, bin.length);
  }
  public TMessage readMessageBegin() throws TException {
    int size = readI32();
    if (size < 0) {
      int version = size & VERSION_MASK;
      if (version != VERSION_1) {
        throw new TProtocolException(
            TProtocolException.BAD_VERSION, "Bad version in readMessageBegin");
      }
      return new TMessage(readString(), (byte) (size & 0x000000ff), readI32());
    } else {
      if (strictRead_) {
        throw new TProtocolException(
            TProtocolException.BAD_VERSION, "Missing version in readMessageBegin, old client?");
      }
      return new TMessage(readStringBody(size), readByte(), readI32());
    }
  }
  public void readMessageEnd() {}
  public TStruct readStructBegin(
      Map<Integer, com.facebook.thrift.meta_data.FieldMetaData> metaDataMap) {
    return ANONYMOUS_STRUCT;
  }
  public void readStructEnd() {}
  public TField readFieldBegin() throws TException {
    byte type = readByte();
    short id = type == TType.STOP ? 0 : readI16();
    return new TField("", type, id);
  }
  public void readFieldEnd() {}
  public TMap readMapBegin() throws TException {
    byte keyType = readByte();
    byte valueType = readByte();
    int size = readI32();
    ensureMapHasEnough(size, keyType, valueType);
    return new TMap(keyType, valueType, size);
  }
  public void readMapEnd() {}
  public TList readListBegin() throws TException {
    byte type = readByte();
    int size = readI32();
    ensureContainerHasEnough(size, type);
    return new TList(type, size);
  }
  public void readListEnd() {}
  public TSet readSetBegin() throws TException {
    byte type = readByte();
    int size = readI32();
    ensureContainerHasEnough(size, type);
    return new TSet(type, size);
  }
  public void readSetEnd() {}
  public boolean readBool() throws TException {
    return (readByte() == 1);
  }
  public byte readByte() throws TException {
    if (trans_.getBytesRemainingInBuffer() >= 1) {
      byte b = trans_.getBuffer()[trans_.getBufferPosition()];
      trans_.consumeBuffer(1);
      return b;
    }
    readAll(buffer, 0, 1);
    return buffer[0];
  }
  public short readI16() throws TException {
    byte[] buf = buffer;
    int off = 0;
    if (trans_.getBytesRemainingInBuffer() >= 2) {
      buf = trans_.getBuffer();
      off = trans_.getBufferPosition();
      trans_.consumeBuffer(2);
    } else {
      readAll(buffer, 0, 2);
    }
    return (short) (((buf[off] & 0xff) << 8) | ((buf[off + 1] & 0xff)));
  }
  public int readI32() throws TException {
    byte[] buf = buffer;
    int off = 0;
    if (trans_.getBytesRemainingInBuffer() >= 4) {
      buf = trans_.getBuffer();
      off = trans_.getBufferPosition();
      trans_.consumeBuffer(4);
    } else {
      readAll(buffer, 0, 4);
    }
    return ((buf[off] & 0xff) << 24)
        | ((buf[off + 1] & 0xff) << 16)
        | ((buf[off + 2] & 0xff) << 8)
        | ((buf[off + 3] & 0xff));
  }
  public long readI64() throws TException {
    byte[] buf = buffer;
    int off = 0;
    if (trans_.getBytesRemainingInBuffer() >= 8) {
      buf = trans_.getBuffer();
      off = trans_.getBufferPosition();
      trans_.consumeBuffer(8);
    } else {
      readAll(buffer, 0, 8);
    }
    return ((long) (buf[off] & 0xff) << 56)
        | ((long) (buf[off + 1] & 0xff) << 48)
        | ((long) (buf[off + 2] & 0xff) << 40)
        | ((long) (buf[off + 3] & 0xff) << 32)
        | ((long) (buf[off + 4] & 0xff) << 24)
        | ((long) (buf[off + 5] & 0xff) << 16)
        | ((long) (buf[off + 6] & 0xff) << 8)
        | ((long) (buf[off + 7] & 0xff));
  }
  public double readDouble() throws TException {
    return Double.longBitsToDouble(readI64());
  }
  public float readFloat() throws TException {
    return Float.intBitsToFloat(readI32());
  }
  public String readString() throws TException {
    int size = readI32();
    checkReadLength(size);
    if (trans_.getBytesRemainingInBuffer() >= size) {
      String s =
          new String(trans_.getBuffer(), trans_.getBufferPosition(), size, StandardCharsets.UTF_8);
      trans_.consumeBuffer(size);
      return s;
    }
    return readStringBody(size);
  }
  public String readStringBody(int size) throws TException {
    ensureContainerHasEnough(size, TType.BYTE);
    checkReadLength(size);
    byte[] buf = new byte[size];
    trans_.readAll(buf, 0, size);
    return new String(buf, StandardCharsets.UTF_8);
  }
  public byte[] readBinary() throws TException {
    int size = readI32();
    ensureContainerHasEnough(size, TType.BYTE);
    checkReadLength(size);
    byte[] buf = new byte[size];
    trans_.readAll(buf, 0, size);
    return buf;
  }
  private int readAll(byte[] buf, int off, int len) throws TException {
    checkReadLength(len);
    return trans_.readAll(buf, off, len);
  }
  public void setReadLength(int readLength) {
    readLength_ = readLength;
    checkReadLength_ = true;
  }
  protected void checkReadLength(int length) throws TException {
    if (length < 0) {
      throw new TException("Negative length: " + length);
    }
    if (checkReadLength_) {
      readLength_ -= length;
      if (readLength_ < 0) {
        throw new TException("Message length exceeded: " + length);
      }
    }
  }
  @Override
  protected int typeMinimumSize(byte type) {
    switch (type & 0x0f) {
      case TType.BOOL:
      case TType.BYTE:
        return 1;
      case TType.I16:
        return 2;
      case TType.I32:
      case TType.FLOAT:
        return 4;
      case TType.DOUBLE:
      case TType.I64:
        return 8;
      case TType.STRING:
        return 4;
      case TType.LIST:
      case TType.SET:
        return 1 + 4;
      case TType.MAP:
        return 1 + 1 + 4;
      case TType.STRUCT:
        return 1;
      default:
        throw new TProtocolException(
            TProtocolException.INVALID_DATA, "Unexpected data type " + (byte) (type & 0x0f));
    }
  }
}
