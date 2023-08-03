
package testcases.CWE369_Divide_by_Zero.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_zero_modulo_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        int data = 0;
        data = 0; 
        badPublicStatic = true;
        (new CWE369_Divide_by_Zero__int_zero_modulo_22b()).badSink(data );
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
        data = 0; 
        goodB2G1PublicStatic = false;
        (new CWE369_Divide_by_Zero__int_zero_modulo_22b()).goodB2G1Sink(data );
    }
    private void goodB2G2() throws Throwable
    {
        int data = 0;
        data = 0; 
        goodB2G2PublicStatic = true;
        (new CWE369_Divide_by_Zero__int_zero_modulo_22b()).goodB2G2Sink(data );
    }
    private void goodG2B() throws Throwable
    {
        int data = 0;
        data = 2;
        goodG2BPublicStatic = true;
        (new CWE369_Divide_by_Zero__int_zero_modulo_22b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
