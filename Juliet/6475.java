
package testcases.CWE506_Embedded_Malicious_Code;
import testcasesupport.*;
import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class CWE506_Embedded_Malicious_Code__email_04 extends AbstractTestCaseBadOnly
{
    private static final boolean PRIVATE_STATIC_FINAL_TRUE = true;
    public void bad() throws Throwable
    {
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            Properties properties = new Properties();
            Session session = Session.getDefaultInstance(properties, null);
            String messageBody = "...";
            try
            {
                Message message = new MimeMessage(session); 
                message.setFrom(new InternetAddress("sender@example.com"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress("recipient@example.com"));
                message.setSubject("Shhh, I'm sending some bad stuff!");
                message.setText(messageBody);
                Transport.send(message);
            }
            catch (AddressException exceptAddress)
            {
                IO.logger.log(Level.WARNING, "Address is formatted incorrectly", exceptAddress);
            }
            catch (MessagingException exceptMessaging)
            {
                IO.logger.log(Level.WARNING, "Error sending message", exceptMessaging);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}