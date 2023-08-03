
package testcases.CWE581_Object_Model_Violation;
import testcasesupport.*;
public class CWE581_Object_Model_Violation__equals_01_good1 extends AbstractTestCaseClassIssueGood 
{
    private String message = "test";
    public void setMessage(String message) 
    {
        this.message = message;
    }
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (object == null)
        {
            return false;
        }
        if (object.getClass() != this.getClass())
        {
            return false;
        }
        CWE581_Object_Model_Violation__equals_01_good1 objectGood1 = (CWE581_Object_Model_Violation__equals_01_good1)object;
        if (objectGood1.message.equals(this.message))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private int seed = 12345; 
    public int hashCode() 
    {
        return message.hashCode() + seed;
    }
    private void good1() 
    { 
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
