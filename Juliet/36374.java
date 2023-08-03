
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__console_readLine_ArrayList_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        int data;
        badPublicStatic = true;
        data = (new CWE789_Uncontrolled_Mem_Alloc__console_readLine_ArrayList_22b()).badSource();
        ArrayList intArrayList = new ArrayList(data);
    }
    public static boolean goodG2B1PublicStatic = false;
    public static boolean goodG2B2PublicStatic = false;
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        goodG2B1PublicStatic = false;
        data = (new CWE789_Uncontrolled_Mem_Alloc__console_readLine_ArrayList_22b()).goodG2B1Source();
        ArrayList intArrayList = new ArrayList(data);
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        goodG2B2PublicStatic = true;
        data = (new CWE789_Uncontrolled_Mem_Alloc__console_readLine_ArrayList_22b()).goodG2B2Source();
        ArrayList intArrayList = new ArrayList(data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
