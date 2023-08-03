
package testcases.CWE491_Object_Hijack;
import testcasesupport.*;
import java.util.Date;
public class CWE491_Object_Hijack__basic_01_good1 extends AbstractTestCaseClassIssueGood implements Cloneable 
{
    private Date theDate = new Date();
    protected final Object clone() throws CloneNotSupportedException 
    {
        CWE491_Object_Hijack__basic_01_good1 objectGood1 = (CWE491_Object_Hijack__basic_01_good1) super.clone();     
        objectGood1.setDate(new Date(theDate.getTime()));
        return objectGood1;
    }
    public void setDate(Date theDate) 
    {
        this.theDate = theDate;
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
