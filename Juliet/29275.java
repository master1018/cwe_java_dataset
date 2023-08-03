
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE191_Integer_Underflow__int_random_sub_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        int data = 0;
        data = (new SecureRandom()).nextInt();
        badPublicStatic = true;
        (new CWE191_Integer_Underflow__int_random_sub_22b()).badSink(data );
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
        int data = 0;
        data = (new SecureRandom()).nextInt();
        goodB2G1PublicStatic = false;
        (new CWE191_Integer_Underflow__int_random_sub_22b()).goodB2G1Sink(data );
    }
    private void goodB2G2() throws Throwable
    {
        int data = 0;
        data = (new SecureRandom()).nextInt();
        goodB2G2PublicStatic = true;
        (new CWE191_Integer_Underflow__int_random_sub_22b()).goodB2G2Sink(data );
    }
    private void goodG2B() throws Throwable
    {
        int data = 0;
        data = 2;
        goodG2BPublicStatic = true;
        (new CWE191_Integer_Underflow__int_random_sub_22b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
