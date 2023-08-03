
package testcases.CWE506_Embedded_Malicious_Code;
import java.lang.reflect.Field;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
        Field field = String.class.getDeclaredField("value");
        field.setAccessible(true);
        field.set(READONLY_VARIABLE, "Sorry, but I've changed.".toCharArray());
        IO.writeLine(READONLY_VARIABLE);
    }
    private void good1() 
    {        
        IO.writeLine(READONLY_VARIABLE);
    }
    public void good()  
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
