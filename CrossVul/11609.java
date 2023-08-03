
package com.facebook.buck.cli;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;
import com.facebook.buck.apple.AppleNativeIntegrationTestUtils;
import com.facebook.buck.apple.toolchain.ApplePlatform;
import com.facebook.buck.testutil.ProcessResult;
import com.facebook.buck.testutil.TemporaryPaths;
import com.facebook.buck.testutil.integration.ProjectWorkspace;
import com.facebook.buck.testutil.integration.TestContext;
import com.facebook.buck.testutil.integration.TestDataHelper;
import com.facebook.buck.util.HumanReadableException;
import com.facebook.buck.util.NamedTemporaryFile;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
public class ParserCacheCommandIntegrationTest {
  @Rule public TemporaryPaths tmp = new TemporaryPaths();
  @Rule public ExpectedException thrown = ExpectedException.none();
  @Test
  public void testSaveAndLoad() throws IOException {
    assumeTrue(AppleNativeIntegrationTestUtils.isApplePlatformAvailable(ApplePlatform.MACOSX));
    ProjectWorkspace workspace =
        TestDataHelper.createProjectWorkspaceForScenario(this, "parser_with_cell", tmp);
    workspace.setUp();
    TestContext context = new TestContext();
    ProcessResult runBuckResult =
        workspace.runBuckdCommand(context, "query", "deps(
    runBuckResult.assertSuccess();
    assertThat(
        runBuckResult.getStdout(),
        Matchers.containsString(
            "
                + "
                + "
                + "bar
    NamedTemporaryFile tempFile = new NamedTemporaryFile("parser_data", null);
    runBuckResult =
        workspace.runBuckdCommand(context, "parser-cache", "--save", tempFile.get().toString());
    runBuckResult.assertSuccess();
    Path path = tmp.getRoot().resolve("Apps/BUCK");
    byte[] data = {};
    Files.write(path, data);
    context = new TestContext();
    runBuckResult =
        workspace.runBuckdCommand(context, "parser-cache", "--load", tempFile.get().toString());
    runBuckResult.assertSuccess();
    runBuckResult = workspace.runBuckdCommand(context, "query", "deps(
    runBuckResult.assertSuccess();
    assertThat(
        runBuckResult.getStdout(),
        Matchers.containsString(
            "
                + "
                + "
                + "bar
  }
  @Test
  public void testInvalidate() throws IOException {
    ProjectWorkspace workspace =
        TestDataHelper.createProjectWorkspaceForScenario(this, "parser_with_cell", tmp);
    workspace.setUp();
    TestContext context = new TestContext();
    ProcessResult runBuckResult =
        workspace.runBuckdCommand(context, "query", "deps(
    runBuckResult.assertSuccess();
    assertThat(
        runBuckResult.getStdout(),
        Matchers.containsString(
            "
                + "
                + "
                + "bar
    NamedTemporaryFile tempFile = new NamedTemporaryFile("parser_data", null);
    runBuckResult =
        workspace.runBuckdCommand(context, "parser-cache", "--save", tempFile.get().toString());
    runBuckResult.assertSuccess();
    Path path = tmp.getRoot().resolve("Apps/BUCK");
    byte[] data = {};
    Files.write(path, data);
    Path invalidationJsonPath = tmp.getRoot().resolve("invalidation-data.json");
    String jsonData = "[{\"path\":\"Apps/BUCK\",\"status\":\"M\"}]";
    Files.write(invalidationJsonPath, jsonData.getBytes(StandardCharsets.UTF_8));
    context = new TestContext();
    runBuckResult =
        workspace.runBuckdCommand(
            context,
            "parser-cache",
            "--load",
            tempFile.get().toString(),
            "--changes",
            invalidationJsonPath.toString());
    runBuckResult.assertSuccess();
    try {
      workspace.runBuckdCommand(context, "query", "deps(
    } catch (HumanReadableException e) {
      assertThat(
          e.getMessage(), Matchers.containsString("
    }
  }
  @Test
  public void testInvalidData() throws IOException {
    Map<String, String> invalidData = new HashMap();
    invalidData.put("foo", "bar");
    NamedTemporaryFile tempFile = new NamedTemporaryFile("invalid_parser_data", null);
    try (FileOutputStream fos = new FileOutputStream(tempFile.get().toString());
        ZipOutputStream zipos = new ZipOutputStream(fos)) {
      zipos.putNextEntry(new ZipEntry("parser_data"));
      try (ObjectOutputStream oos = new ObjectOutputStream(zipos)) {
        oos.writeObject(invalidData);
      }
    }
    ProjectWorkspace workspace =
        TestDataHelper.createProjectWorkspaceForScenario(this, "parser_with_cell", tmp);
    workspace.setUp();
    thrown.expect(InvalidClassException.class);
    thrown.expectMessage("Can't deserialize this class");
    workspace.runBuckCommand("parser-cache", "--load", tempFile.get().toString());
  }
}
