
package hudson.tasks;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Item;
import hudson.model.Result;
import hudson.model.Run;
import hudson.security.AuthorizationMatrixProperty;
import hudson.security.LegacySecurityRealm;
import hudson.security.Permission;
import hudson.security.ProjectMatrixAuthorizationStrategy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import jenkins.model.Jenkins;
import org.jvnet.hudson.test.ExtractResourceSCM;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.MockBuilder;
public class BuildTriggerTest extends HudsonTestCase {
    private FreeStyleProject createDownstreamProject() throws Exception {
        FreeStyleProject dp = createFreeStyleProject("downstream");
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage(dp,"configure");
        HtmlForm form = page.getFormByName("config");
        form.getInputByName("hasCustomQuietPeriod").click();
        form.getInputByName("quiet_period").setValueAttribute("0");
        submit(form);
        assertEquals("set quiet period", 0, dp.getQuietPeriod());
        return dp;
    }
    private void doTriggerTest(boolean evenWhenUnstable, Result triggerResult,
            Result dontTriggerResult) throws Exception {
        FreeStyleProject p = createFreeStyleProject(),
                dp = createDownstreamProject();
        p.getPublishersList().add(new BuildTrigger("downstream", evenWhenUnstable));
        p.getBuildersList().add(new MockBuilder(dontTriggerResult));
        jenkins.rebuildDependencyGraph();
        FreeStyleBuild b = p.scheduleBuild2(0).get();
        assertNoDownstreamBuild(dp, b);
        p.getBuildersList().replace(new MockBuilder(triggerResult));
        b = p.scheduleBuild2(0).get();
        assertDownstreamBuild(dp, b);
    }
    private void assertNoDownstreamBuild(FreeStyleProject dp, Run<?,?> b) throws Exception {
        for (int i = 0; i < 3; i++) {
            Thread.sleep(200);
            assertTrue("downstream build should not run!  upstream log: " + getLog(b),
                       !dp.isInQueue() && !dp.isBuilding() && dp.getLastBuild()==null);
        }
    }
    private void assertDownstreamBuild(FreeStyleProject dp, Run<?,?> b) throws Exception {
        for (int i = 0; dp.getLastBuild()==null && i < 20; i++) Thread.sleep(100);
        assertNotNull("downstream build didn't run.. upstream log: " + getLog(b), dp.getLastBuild());
    }
    public void testBuildTrigger() throws Exception {
        doTriggerTest(false, Result.SUCCESS, Result.UNSTABLE);
    }
    public void testTriggerEvenWhenUnstable() throws Exception {
        doTriggerTest(true, Result.UNSTABLE, Result.FAILURE);
    }
    private void doMavenTriggerTest(boolean evenWhenUnstable) throws Exception {
        FreeStyleProject dp = createDownstreamProject();
        configureDefaultMaven();
        MavenModuleSet m = createMavenProject();
        m.getPublishersList().add(new BuildTrigger("downstream", evenWhenUnstable));
        if (!evenWhenUnstable) {
            m.setGoals("clean test");
            m.setScm(new ExtractResourceSCM(getClass().getResource("maven-test-failure.zip")));
        } 
        MavenModuleSetBuild b = m.scheduleBuild2(0).get();
        assertNoDownstreamBuild(dp, b);
        if (evenWhenUnstable) {
            m.setGoals("clean test");
            m.setScm(new ExtractResourceSCM(getClass().getResource("maven-test-failure.zip")));
        } else {
            m.setGoals("clean");
            m.setScm(new ExtractResourceSCM(getClass().getResource("maven-empty.zip")));
        }
        b = m.scheduleBuild2(0).get();
        assertDownstreamBuild(dp, b);
    }
    public void testMavenBuildTrigger() throws Exception {
        doMavenTriggerTest(false);
    }
    public void testMavenTriggerEvenWhenUnstable() throws Exception {
        doMavenTriggerTest(true);
    }
    public void testConfigureDownstreamProjectSecurity() throws Exception {
        jenkins.setSecurityRealm(new LegacySecurityRealm());
        ProjectMatrixAuthorizationStrategy auth = new ProjectMatrixAuthorizationStrategy();
        auth.add(Jenkins.READ, "alice");
        jenkins.setAuthorizationStrategy(auth);
        FreeStyleProject upstream = createFreeStyleProject("upstream");
        Map<Permission,Set<String>> perms = new HashMap<Permission,Set<String>>();
        perms.put(Item.READ, Collections.singleton("alice"));
        perms.put(Item.CONFIGURE, Collections.singleton("alice"));
        upstream.addProperty(new AuthorizationMatrixProperty(perms));
        FreeStyleProject downstream = createFreeStyleProject("downstream");
        WebClient wc = createWebClient();
        wc.login("alice");
        HtmlPage page = wc.getPage(upstream, "configure");
        HtmlForm config = page.getFormByName("config");
        config.getButtonByCaption("Add post-build action").click(); 
        page.getAnchorByText("Build other projects").click();
        HtmlTextInput childProjects = config.getInputByName("buildTrigger.childProjects");
        childProjects.setValueAttribute("downstream");
        try {
            submit(config);
            fail();
        } catch (FailingHttpStatusCodeException x) {
            assertEquals(403, x.getStatusCode());
        }
        assertEquals(Collections.emptyList(), upstream.getDownstreamProjects());
    }
}
