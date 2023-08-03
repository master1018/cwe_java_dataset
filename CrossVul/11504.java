
package org.dashbuilder.dataprovider.sql;
import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
public class SQLTestSuite extends SQLDataSetTestBase {
    protected <T extends SQLDataSetTestBase> T setUp(T test) throws Exception {
        test.testSettings = testSettings;
        test.conn = conn;
        return test;
    }
    protected List<SQLDataSetTestBase> sqlTestList = new ArrayList<SQLDataSetTestBase>();
    @Before
    public void setUp() throws Exception {
        super.setUp();
        sqlTestList.add(setUp(new SQLDataSetDefTest()));
        sqlTestList.add(setUp(new SQLDataSetTrimTest()));
        sqlTestList.add(setUp(new SQLTableDataSetLookupTest()));
        sqlTestList.add(setUp(new SQLQueryDataSetLookupTest()));
        sqlTestList.add(setUp(new SQLInjectionAttacksTest()));
    }
    public void testAll() throws Exception {
        for (SQLDataSetTestBase testBase : sqlTestList) {
            testBase.testAll();
        }
    }
}
