
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_22b
{
    public void badSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_22a.badPublicStatic)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
            if (data != 0)
            {
                IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
        }
    }
    public void goodB2G2Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_22a.goodB2G2PublicStatic)
        {
            if (data != 0)
            {
                IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
        }
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_22a.goodG2BPublicStatic)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
        else
        {
            data = 0;
        }
    }
}
