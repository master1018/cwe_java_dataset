
package testcases.CWE369_Divide_by_Zero.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_zero_divide_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 0; 
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE369_Divide_by_Zero__int_zero_divide_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE369_Divide_by_Zero__int_zero_divide_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = 0; 
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE369_Divide_by_Zero__int_zero_divide_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
