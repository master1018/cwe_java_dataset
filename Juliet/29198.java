
package testcases.CWE582_Array_Public_Final_Static;
import testcasesupport.*;
public class CWE582_Array_Public_Final_Static__basic_01_good1 extends AbstractTestCaseClassIssueGood
{
    private final static int INT_ARRAY[] = {1,2,3,4,5}; 
    private void good1() 
    { 
        IO.writeLine("INT_ARRAY[0]: " + Integer.toString(CWE582_Array_Public_Final_Static__basic_01_good1.INT_ARRAY[0]));
        CWE582_Array_Public_Final_Static__basic_01_good1.INT_ARRAY[0] = 2;
        IO.writeLine("INT_ARRAY[0]: " + Integer.toString(CWE582_Array_Public_Final_Static__basic_01_good1.INT_ARRAY[0]));
    }
    public void good() 
    {
        good1();
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
