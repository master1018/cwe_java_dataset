
package com.facebook.thrift;
import com.facebook.thrift.java.test.MyListStruct;
import com.facebook.thrift.java.test.MyMapStruct;
import com.facebook.thrift.java.test.MySetStruct;
import com.facebook.thrift.java.test.MyStringStruct;
import com.facebook.thrift.protocol.TBinaryProtocol;
import com.facebook.thrift.protocol.TCompactProtocol;
import com.facebook.thrift.protocol.TProtocol;
import com.facebook.thrift.protocol.TProtocolException;
import com.facebook.thrift.protocol.TType;
import com.facebook.thrift.transport.TMemoryInputTransport;
import org.junit.Test;
public class TruncatedFrameTest extends junit.framework.TestCase {
  private static final byte[] kBinaryListEncoding = {
    TType.LIST, 
    (byte) 0x00,
    (byte) 0x01, 
    TType.I64, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0xFF, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x01, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x02, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x03, 
    (byte) 0x00, 
  };
  private static final byte[] kCompactListEncoding = {
    (byte) 0b00011001, 
    (byte) 0b11100110, 
    (byte) 0x02, 
    (byte) 0x04, 
    (byte) 0x06, 
    (byte) 0x00, 
  };
  private static final byte[] kCompactListEncoding2 = {
    (byte) 0b00011001, 
    (byte) 0b11110110, 
    (byte) 0x64, 
    (byte) 0x02, 
    (byte) 0x04, 
    (byte) 0x06, 
    (byte) 0x00, 
  };
  public static void testTruncated(TBase struct, TProtocol iprot) throws Exception {
    try {
      struct.read(iprot);
      assertTrue("Not reachable", false);
    } catch (TProtocolException ex) {
      assertEquals(
          "Not enough bytes to read the entire message, the data appears to be truncated",
          ex.getMessage());
    }
  }
  @Test
  public static void testListBinary() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kBinaryListEncoding);
    TProtocol iprot = new TBinaryProtocol(buf);
    testTruncated(new MyListStruct(), iprot);
  }
  @Test
  public static void testListCompact() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kCompactListEncoding);
    TProtocol iprot = new TCompactProtocol(buf);
    testTruncated(new MyListStruct(), iprot);
  }
  @Test
  public static void testLongListCompact() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kCompactListEncoding2);
    TProtocol iprot = new TCompactProtocol(buf);
    testTruncated(new MyListStruct(), iprot);
  }
  private static final byte[] kBinarySetEncoding = {
    TType.SET, 
    (byte) 0x00,
    (byte) 0x01, 
    TType.I64, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0xFF, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x01, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x02, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x03, 
    (byte) 0x00, 
  };
  private static final byte[] kCompactSetEncoding = {
    (byte) 0b00011010, 
    (byte) 0b01110110, 
    (byte) 0x02, 
    (byte) 0x04, 
    (byte) 0x06, 
    (byte) 0x00, 
  };
  private static final byte[] kCompactSetEncoding2 = {
    (byte) 0b00011010, 
    (byte) 0b11110110, 
    (byte) 0x64, 
    (byte) 0x02, 
    (byte) 0x04, 
    (byte) 0x06, 
    (byte) 0x00, 
  };
  @Test
  public static void testSetBinary() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kBinarySetEncoding);
    TProtocol iprot = new TBinaryProtocol(buf);
    testTruncated(new MySetStruct(), iprot);
  }
  @Test
  public static void testSetCompact() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kCompactSetEncoding);
    TProtocol iprot = new TCompactProtocol(buf);
    testTruncated(new MySetStruct(), iprot);
  }
  @Test
  public static void testLongSetCompact() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kCompactSetEncoding2);
    TProtocol iprot = new TCompactProtocol(buf);
    testTruncated(new MySetStruct(), iprot);
  }
  private static final byte[] kBinaryMapEncoding = {
    TType.MAP, 
    (byte) 0x00,
    (byte) 0x01, 
    TType.I64, 
    TType.STRING, 
    (byte) 0x00,
    (byte) 0xFF,
    (byte) 0xFF,
    (byte) 0xFF, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x01, 
    (byte) 0x30, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x01, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x01, 
    (byte) 0x31, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x02, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x01, 
    (byte) 0x32, 
    (byte) 0x00, 
  };
  private static final byte[] kCompactMapEncoding = {
    (byte) 0b00011011, 
    (byte) 0x64, 
    (byte) 0b01101000, 
    (byte) 0x00, 
    (byte) 0x01, 
    (byte) 0x30, 
    (byte) 0x02, 
    (byte) 0x01, 
    (byte) 0x31, 
    (byte) 0x04, 
    (byte) 0x01, 
    (byte) 0x32, 
    (byte) 0x00, 
  };
  @Test
  public static void testMapBinary() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kBinaryMapEncoding);
    TProtocol iprot = new TBinaryProtocol(buf);
    testTruncated(new MyMapStruct(), iprot);
  }
  @Test
  public static void testMapCompact() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kCompactMapEncoding);
    TProtocol iprot = new TCompactProtocol(buf);
    testTruncated(new MyMapStruct(), iprot);
  }
  private static final byte[] kBinaryStringEncoding = {
    TType.STRING, 
    (byte) 0x00,
    (byte) 0x01, 
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0x00,
    (byte) 0xFF, 
    (byte) 0x48,
    (byte) 0x65,
    (byte) 0x6C,
    (byte) 0x6C,
    (byte) 0x6F,
    (byte) 0x2C,
    (byte) 0x20,
    (byte) 0x57,
    (byte) 0x6F,
    (byte) 0x72,
    (byte) 0x6C,
    (byte) 0x64,
    (byte) 0x21, 
    (byte) 0x00, 
  };
  private static final byte[] kCompactStringEncoding = {
    (byte) 0b00011000, 
    (byte) 0xFF,
    (byte) 0x0F, 
    (byte) 0x48,
    (byte) 0x65,
    (byte) 0x6C,
    (byte) 0x6C,
    (byte) 0x6F,
    (byte) 0x2C,
    (byte) 0x20,
    (byte) 0x57,
    (byte) 0x6F,
    (byte) 0x72,
    (byte) 0x6C,
    (byte) 0x64,
    (byte) 0x21, 
    (byte) 0x00, 
  };
  @Test
  public void testStringBinary() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kBinaryStringEncoding);
    TProtocol iprot = new TBinaryProtocol(buf);
    testTruncated(new MyStringStruct(), iprot);
  }
  @Test
  public void testStringCompact() throws Exception {
    TMemoryInputTransport buf = new TMemoryInputTransport(kCompactStringEncoding);
    TProtocol iprot = new TCompactProtocol(buf);
    testTruncated(new MyStringStruct(), iprot);
  }
}
