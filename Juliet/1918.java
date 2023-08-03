
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        int data;
        badPublicStatic = true;
        data = (new CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_22b()).badSource();
        HashSet intHashSet = new HashSet(data);
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
        data = (new CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_22b()).goodG2B1Source();
        HashSet intHashSet = new HashSet(data);
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        goodG2B2PublicStatic = true;
        data = (new CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_22b()).goodG2B2Source();
        HashSet intHashSet = new HashSet(data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
