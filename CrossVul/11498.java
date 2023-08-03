
package bsh;
import org.junit.Assert;
import org.junit.Test;
public class BshSerializationTest {
    @Test
    public void testNullValueSerialization() throws Exception {
        final Interpreter origInterpreter = new Interpreter();
        origInterpreter.eval("myNull = null;");
        Assert.assertNull(origInterpreter.eval("myNull"));
        final Interpreter deserInterpreter = TestUtil.serDeser(origInterpreter);
        Assert.assertNull(deserInterpreter.eval("myNull"));
    }
    @Test
    public void testSpecialNullSerialization() throws Exception {
        final Interpreter originalInterpreter = new Interpreter();
        originalInterpreter.eval("myNull = null;");
        Assert.assertTrue((Boolean) originalInterpreter.eval("myNull == null"));
        final Interpreter deserInterpreter = TestUtil.serDeser(originalInterpreter);
        Assert.assertTrue((Boolean) deserInterpreter.eval("myNull == null"));
    }
    @Test
    public void testMethodSerialization() throws Exception {
        final Interpreter origInterpreter = new Interpreter();
        origInterpreter.eval("int method() { return 1337; }");
        Assert.assertEquals(1337, origInterpreter.eval("method()"));
        final Interpreter deserInterpreter = TestUtil.serDeser(origInterpreter);
        Assert.assertEquals(1337, deserInterpreter.eval("method()"));
    }
}
