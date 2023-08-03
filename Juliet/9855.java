
package testcases.CWE607_Public_Static_Final_Mutable;
import java.util.Calendar;
import java.util.Date;
import testcasesupport.*;
public class CWE607_Public_Static_Final_Mutable__console_01_good1 extends AbstractTestCaseClassIssueGood 
{
    private final static Date date = new Date(); 
    public String getHello()
    {
        return "Hello!" + date.getTime();
    }
    public final Date getDate()
    {
        return (Date) date.clone(); 
    }
    public void good() 
    {
        IO.writeLine("Before: " + this.getHello());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 01, 01);
        this.getDate().setTime(calendar.getTimeInMillis());
        IO.writeLine("After: " + this.getHello());
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
