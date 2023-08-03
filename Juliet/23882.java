
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        int data;
        badPublicStatic = true;
        data = (new CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_22b()).badSource();
        HashMap intHashMap = new HashMap(data);
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
        data = (new CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_22b()).goodG2B1Source();
        HashMap intHashMap = new HashMap(data);
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        goodG2B2PublicStatic = true;
        data = (new CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_22b()).goodG2B2Source();
        HashMap intHashMap = new HashMap(data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
