package org.zeroturnaround.zip;
import java.io.File;
import junit.framework.TestCase;
public class DirectoryTraversalMaliciousTest extends TestCase {
  private static final File badFile = new File("src/test/resources/zip-malicious-traversal.zip");
  private static final File badFileBackslashes = new File("src/test/resources/zip-malicious-traversal-backslashes.zip");
  public void testUnpackDoesntLeaveTarget() throws Exception {
    File file = File.createTempFile("temp", null);
    File tmpDir = file.getParentFile();
    try {
      ZipUtil.unpack(badFile, tmpDir);
      fail();
    }
    catch (ZipException e) {
      assertTrue(true);
    }
  }
  public void testUnwrapDoesntLeaveTarget() throws Exception {
    File file = File.createTempFile("temp", null);
    File tmpDir = file.getParentFile();
    try {
      ZipUtil.iterate(badFileBackslashes, new ZipUtil.BackslashUnpacker(tmpDir));
      fail();
    }
    catch (ZipException e) {
      assertTrue(true);
    }
  }
}
