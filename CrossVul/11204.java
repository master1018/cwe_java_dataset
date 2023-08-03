
package hudson.model;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
public class PasswordParameterDefinitionTest {
    @Rule public JenkinsRule j = new JenkinsRule();
    @Test public void defaultValueKeptSecret() throws Exception {
        FreeStyleProject p = j.createFreeStyleProject();
        p.addProperty(new ParametersDefinitionProperty(new PasswordParameterDefinition("p", "s3cr3t", "")));
        j.configRoundtrip(p);
        assertEquals("s3cr3t", ((PasswordParameterDefinition) p.getProperty(ParametersDefinitionProperty.class).getParameterDefinition("p")).getDefaultValue());
    }
}
