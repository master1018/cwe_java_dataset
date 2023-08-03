
package testcases.CWE566_Authorization_Bypass_Through_SQL_Primary;
import testcasesupport.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.logging.Level;
public class CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_22a extends AbstractTestCaseServlet
{
    public static boolean badPublicStatic = false;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        badPublicStatic = true;
        data = (new CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_22b()).badSource(request, response);
        Connection dBConnection = IO.getDBConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = 0;
        try
        {
            id = Integer.parseInt(data);
        }
        catch ( NumberFormatException nfx )
        {
            id = -1; 
        }
        try
        {
            preparedStatement = dBConnection.prepareStatement("select * from invoices where uid=?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            IO.writeString("bad() - result requested: " + data +"\n");
        }
        catch (SQLException exceptSql)
        {
            IO.logger.log(Level.WARNING, "Error executing query", exceptSql);
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
                IO.logger.log(Level.WARNING, "Could not close ResultSet", exceptSql);
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
                IO.logger.log(Level.WARNING, "Could not close PreparedStatement", exceptSql);
            }
            try
            {
                if (dBConnection != null)
                {
                    dBConnection.close();
                }
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Could not close Connection", exceptSql);
            }
        }
    }
    public static boolean goodG2B1PublicStatic = false;
    public static boolean goodG2B2PublicStatic = false;
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B1(request, response);
        goodG2B2(request, response);
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        goodG2B1PublicStatic = false;
        data = (new CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_22b()).goodG2B1Source(request, response);
        Connection dBConnection = IO.getDBConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = 0;
        try
        {
            id = Integer.parseInt(data);
        }
        catch ( NumberFormatException nfx )
        {
            id = -1; 
        }
        try
        {
            preparedStatement = dBConnection.prepareStatement("select * from invoices where uid=?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            IO.writeString("bad() - result requested: " + data +"\n");
        }
        catch (SQLException exceptSql)
        {
            IO.logger.log(Level.WARNING, "Error executing query", exceptSql);
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
                IO.logger.log(Level.WARNING, "Could not close ResultSet", exceptSql);
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
                IO.logger.log(Level.WARNING, "Could not close PreparedStatement", exceptSql);
            }
            try
            {
                if (dBConnection != null)
                {
                    dBConnection.close();
                }
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Could not close Connection", exceptSql);
            }
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        goodG2B2PublicStatic = true;
        data = (new CWE566_Authorization_Bypass_Through_SQL_Primary__Servlet_22b()).goodG2B2Source(request, response);
        Connection dBConnection = IO.getDBConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = 0;
        try
        {
            id = Integer.parseInt(data);
        }
        catch ( NumberFormatException nfx )
        {
            id = -1; 
        }
        try
        {
            preparedStatement = dBConnection.prepareStatement("select * from invoices where uid=?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            IO.writeString("bad() - result requested: " + data +"\n");
        }
        catch (SQLException exceptSql)
        {
            IO.logger.log(Level.WARNING, "Error executing query", exceptSql);
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
                IO.logger.log(Level.WARNING, "Could not close ResultSet", exceptSql);
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
                IO.logger.log(Level.WARNING, "Could not close PreparedStatement", exceptSql);
            }
            try
            {
                if (dBConnection != null)
                {
                    dBConnection.close();
                }
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Could not close Connection", exceptSql);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
