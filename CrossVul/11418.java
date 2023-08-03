
package com.github.junrar.testUtil;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class SimpleTest {
    @Test
    public void testTikaDocs() throws Exception {
        String[] expected = {"testEXCEL.xls", "13824",
                "testHTML.html", "167",
                "testOpenOffice2.odt", "26448",
                "testPDF.pdf", "34824",
                "testPPT.ppt", "16384",
                "testRTF.rtf", "3410",
                "testTXT.txt", "49",
                "testWORD.doc", "19456",
                "testXML.xml", "766"};
        File f = new File(getClass().getResource("test-documents.rar").toURI());
        Archive archive = null;
        try {
            archive = new Archive(f);
            FileHeader fileHeader = archive.nextFileHeader();
            int i = 0;
            while (fileHeader != null) {
                assertTrue(fileHeader.getFileNameString().contains(expected[i++]));
                assertEquals(Long.parseLong(expected[i++]), fileHeader.getUnpSize());
                fileHeader = archive.nextFileHeader();
            }
        } finally {
            archive.close();
        }
    }
}
