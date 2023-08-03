
package com.facebook.thrift;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import com.facebook.thrift.java.test.BigEnum;
import com.facebook.thrift.java.test.MySimpleStruct;
import com.facebook.thrift.java.test.SmallEnum;
import junit.framework.TestCase;
import org.junit.Test;
public class StructTest extends TestCase {
  @Test
  public void testStructHashcode() throws Exception {
    MySimpleStruct defaultStruct = new MySimpleStruct();
    assertThat(defaultStruct.hashCode(), is(not(equalTo(0))));
    MySimpleStruct struct1 = new MySimpleStruct(1, "Foo");
    MySimpleStruct struct2 = new MySimpleStruct(2, "Bar");
    assertThat(struct1.hashCode(), is(not(equalTo(0))));
    assertThat(struct2.hashCode(), is(not(equalTo(0))));
    assertThat(struct1.hashCode(), is(not(equalTo(struct2.hashCode()))));
  }
  @Test
  public void testSmallEnum() throws Exception {
    assertThat(SmallEnum.findByValue(SmallEnum.RED.getValue()), equalTo(SmallEnum.RED));
    assertThat(SmallEnum.findByValue(Integer.MAX_VALUE), equalTo(null));
  }
  @Test
  public void testBigEnum() throws Exception {
    assertThat(BigEnum.findByValue(BigEnum.ONE.getValue()), equalTo(BigEnum.ONE));
    assertThat(BigEnum.findByValue(Integer.MAX_VALUE), equalTo(null));
  }
}
