
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
public class ParserCacheCommandIntegrationTest {
  @Rule public TemporaryPaths tmp = new TemporaryPaths();
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
}
