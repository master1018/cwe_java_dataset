
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__random_write_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        int count = 0;
        count = (new SecureRandom()).nextInt();
        badPublicStatic = true;
        (new CWE400_Resource_Exhaustion__random_write_22b()).badSink(count );
    }
    public static boolean goodB2G1PublicStatic = false;
    public static boolean goodB2G2PublicStatic = false;
    public static boolean goodG2BPublicStatic = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        int count = 0;
        count = (new SecureRandom()).nextInt();
        goodB2G1PublicStatic = false;
        (new CWE400_Resource_Exhaustion__random_write_22b()).goodB2G1Sink(count );
    }
    private void goodB2G2() throws Throwable
    {
        int count = 0;
        count = (new SecureRandom()).nextInt();
        goodB2G2PublicStatic = true;
        (new CWE400_Resource_Exhaustion__random_write_22b()).goodB2G2Sink(count );
    }
    private void goodG2B() throws Throwable
    {
        int count = 0;
        count = 2;
        goodG2BPublicStatic = true;
        (new CWE400_Resource_Exhaustion__random_write_22b()).goodG2BSink(count );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
