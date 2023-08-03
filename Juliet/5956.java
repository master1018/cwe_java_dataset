
package testcases.CWE319_Cleartext_Tx_Sensitive_Info;
import testcasesupport.*;
import java.sql.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.logging.Level;
public class CWE319_Cleartext_Tx_Sensitive_Info__listen_tcp_driverManager_81_goodG2B extends CWE319_Cleartext_Tx_Sensitive_Info__listen_tcp_driverManager_81_base
{
    public void action(String password ) throws Throwable
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try
        {
            connection = DriverManager.getConnection("data-url", "root", password);
            preparedStatement = connection.prepareStatement("select * from test_table");
            resultSet = preparedStatement.executeQuery();
        }
        catch (SQLException exceptSql)
        {
            IO.logger.log(Level.WARNING, "Error with database connection", exceptSql);
        }
        finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error closing ResultSet", exceptSql);
            }
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error closing PreparedStatement", exceptSql);
            }
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
            }
        }
    }
}
