
package testcases.CWE511_Logic_Time_Bomb;
import testcasesupport.*;
import java.util.Calendar;
public class CWE511_Logic_Time_Bomb__time_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarCheck = Calendar.getInstance();
        calendarCheck.set(2020, 1, 1);
        if (calendarNow.after(calendarCheck))
        {
            Runtime.getRuntime().exec("c:\\windows\\system32\\evil.exe");
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarCheck = Calendar.getInstance();
        calendarCheck.set(2020, 1, 1);
        if (calendarNow.after(calendarCheck))
        {
            IO.writeLine("Sorry, your license has expired.  Please contact support.");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
