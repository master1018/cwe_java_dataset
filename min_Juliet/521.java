
package testcases.CWE580_Clone_Without_Super;
import testcasesupport.*;
public class CWE580_Clone_Without_Super__clone_01a extends AbstractTestCaseClassIssue implements Cloneable
{
    {
        badObject = new CWE580_Clone_Without_Super__clone_01_bad();
        good1Object = new CWE580_Clone_Without_Super__clone_01_good1();
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
