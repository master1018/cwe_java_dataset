
package testcases.CWE491_Object_Hijack;
import java.util.Date;
import testcasesupport.*;
public class CWE491_Object_Hijack__basic_01_bad extends AbstractTestCaseClassIssueBad implements Cloneable 
{
    private Date theDate = new Date();
    protected Object clone() throws CloneNotSupportedException 
    {
        CWE491_Object_Hijack__basic_01_bad objectBad = (CWE491_Object_Hijack__basic_01_bad) super.clone();     
        objectBad.setDate(new Date(theDate.getTime()));
        return objectBad;      
    }
    public void setDate(Date theDate) 
    {
        this.theDate = theDate;
    }
    public void bad() 
    {
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
