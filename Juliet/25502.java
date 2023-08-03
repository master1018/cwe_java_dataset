
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__database_HashMap_52c
{
    public void badSink(int data ) throws Throwable
    {
        HashMap intHashMap = new HashMap(data);
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        HashMap intHashMap = new HashMap(data);
    }
}
