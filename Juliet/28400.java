
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__PropertiesFile_ArrayList_51b
{
    public void badSink(int data ) throws Throwable
    {
        ArrayList intArrayList = new ArrayList(data);
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        ArrayList intArrayList = new ArrayList(data);
    }
}