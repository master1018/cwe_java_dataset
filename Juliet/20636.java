
package testcases.CWE561_Dead_Code;
import testcasesupport.AbstractTestCaseClassIssueBad;
import testcasesupport.IO;
public class CWE561_Dead_Code__unused_method_01_bad extends AbstractTestCaseClassIssueBad 
{
    private String calculation()
    {
        return "Calculation";
    }
    public void bad() throws Throwable 
    {
        IO.writeLine("hello");
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
