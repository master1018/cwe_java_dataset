
package com.facebook.thrift.protocol;
import com.facebook.thrift.TException;
import com.facebook.thrift.meta_data.FieldMetaData;
import com.facebook.thrift.scheme.IScheme;
import com.facebook.thrift.scheme.StandardScheme;
import com.facebook.thrift.transport.TTransport;
import java.util.Collections;
import java.util.Map;
public abstract class TProtocol {
  @SuppressWarnings("unused")
  private TProtocol() {}
  protected TTransport trans_;
  protected TProtocol(TTransport trans) {
    trans_ = trans;
  }
  public TTransport getTransport() {
    return trans_;
  }
  public abstract void writeMessageBegin(TMessage message) throws TException;
  public abstract void writeMessageEnd() throws TException;
  public abstract void writeStructBegin(TStruct struct) throws TException;
  public abstract void writeStructEnd() throws TException;
  public abstract void writeFieldBegin(TField field) throws TException;
  public abstract void writeFieldEnd() throws TException;
  public abstract void writeFieldStop() throws TException;
  public abstract void writeMapBegin(TMap map) throws TException;
  public abstract void writeMapEnd() throws TException;
  public abstract void writeListBegin(TList list) throws TException;
  public abstract void writeListEnd() throws TException;
  public abstract void writeSetBegin(TSet set) throws TException;
  public abstract void writeSetEnd() throws TException;
  public abstract void writeBool(boolean b) throws TException;
  public abstract void writeByte(byte b) throws TException;
  public abstract void writeI16(short i16) throws TException;
  public abstract void writeI32(int i32) throws TException;
  public abstract void writeI64(long i64) throws TException;
  public abstract void writeDouble(double dub) throws TException;
  public abstract void writeFloat(float flt) throws TException;
  public abstract void writeString(String str) throws TException;
  public abstract void writeBinary(byte[] bin) throws TException;
  public abstract TMessage readMessageBegin() throws TException;
  public abstract void readMessageEnd() throws TException;
  public abstract TStruct readStructBegin(Map<Integer, FieldMetaData> metaDataMap)
      throws TException;
  public TStruct readStructBegin() throws TException {
    return readStructBegin(Collections.<Integer, FieldMetaData>emptyMap());
  }
  public abstract void readStructEnd() throws TException;
  public abstract TField readFieldBegin() throws TException;
  public abstract void readFieldEnd() throws TException;
  public abstract TMap readMapBegin() throws TException;
  public boolean peekMap() throws TException {
    throw new TException("Peeking into a map not supported, likely because it's sized");
  }
  public abstract void readMapEnd() throws TException;
  public abstract TList readListBegin() throws TException;
  public boolean peekList() throws TException {
    throw new TException("Peeking into a list not supported, likely because it's sized");
  }
  public abstract void readListEnd() throws TException;
  public abstract TSet readSetBegin() throws TException;
  public boolean peekSet() throws TException {
    throw new TException("Peeking into a set not supported, likely because it's sized");
  }
  public abstract void readSetEnd() throws TException;
  public abstract boolean readBool() throws TException;
  public abstract byte readByte() throws TException;
  public abstract short readI16() throws TException;
  public abstract int readI32() throws TException;
  public abstract long readI64() throws TException;
  public abstract double readDouble() throws TException;
  public abstract float readFloat() throws TException;
  public abstract String readString() throws TException;
  public abstract byte[] readBinary() throws TException;
  public void reset() {}
  public Class<? extends IScheme> getScheme() {
    return StandardScheme.class;
  }
  protected int typeMinimumSize(byte type) {
    return 1;
  }
  protected void ensureContainerHasEnough(int size, byte type) {
    int minimumExpected = size * typeMinimumSize(type);
    ensureHasEnoughBytes(minimumExpected);
  }
  protected void ensureMapHasEnough(int size, byte keyType, byte valueType) {
    int minimumExpected = size * (typeMinimumSize(keyType) + typeMinimumSize(valueType));
    ensureHasEnoughBytes(minimumExpected);
  }
  private void ensureHasEnoughBytes(int minimumExpected) {
    int remaining = trans_.getBytesRemainingInBuffer();
    if (remaining < 0) {
      return; 
    }
    if (remaining < minimumExpected) {
      throw new TProtocolException(
          TProtocolException.INVALID_DATA,
          "Not enough bytes to read the entire message, the data appears to be truncated");
    }
  }
}
