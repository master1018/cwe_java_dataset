
package testcases.CWE561_Dead_Code;
import testcasesupport.AbstractTestCaseClassIssueGood;
import testcasesupport.IO;
public class CWE561_Dead_Code__unused_method_01_good1 extends AbstractTestCaseClassIssueGood 
{
    private String calculation()
    {
        return "Calculation";
    }
    public void good() throws Throwable 
    {
        good1();
    }
    private void good1()
    {
        IO.writeLine(calculation());
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
