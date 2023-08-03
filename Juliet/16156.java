
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_public_member_variable_01_good1 extends AbstractTestCaseClassIssueGood 
{
    public int intGood1 = 0;
    private void good1() 
    { 
        IO.writeLine("" + intGood1);
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
