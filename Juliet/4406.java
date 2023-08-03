
package testcases.CWE500_Public_Static_Field_Not_Final;
import testcasesupport.*;
public class CWE500_Public_Static_Field_Not_Final__String_01_good1 extends AbstractTestCaseClassIssueGood implements Cloneable 
{    
    public static final String DEFAULT_ERROR = "The value you entered was not understood.  Please try again.";
    private void good1() 
    { 
        IO.writeLine(DEFAULT_ERROR);
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
