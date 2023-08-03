
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_private_member_value_01_good1 extends AbstractTestCaseClassIssueGood 
{
    private int intGood1 = 5;
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
