
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__int_random_modulo_61b
{
    public int badSource() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        return data;
    }
    public int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
    public int goodB2GSource() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        return data;
    }
}
