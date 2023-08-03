
package testcases.CWE477_Obsolete_Functions;
import testcasesupport.*;
public class CWE477_Obsolete_Functions__String_getBytes_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
        {
            String sentence = "Convert this to bytes";
            byte[] sentenceAsBytes = new byte[sentence.length()];
            sentence.getBytes(0, sentence.length(), sentenceAsBytes, 0);
            IO.writeLine(IO.toHex(sentenceAsBytes)); 
        }
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
        {
            String sentence = "Convert this to bytes";
            byte[] sentenceAsBytes = sentence.getBytes("UTF-8");
            IO.writeLine(IO.toHex(sentenceAsBytes)); 
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
