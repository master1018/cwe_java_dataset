
package org.jboss.weld.tests.contexts.cache;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.weld.tests.category.Integration;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
@RunWith(Arquillian.class)
@Category(Integration.class)
public class RequestScopedCacheLeakTest {
    @ArquillianResource
    private URL contextPath;
    @Deployment(testable = false)
    public static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(SimpleServlet.class, ConversationScopedBean.class);
    }
    @Test
    public void test() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(false);
        for (int i = 0; i < 100; i++) {
            sendRequest(webClient, i, true);
        }
        for (int i = 0; i < 100; i++) {
            String result = sendRequest(webClient, i, false);
            Assert.assertFalse("Invalid state detected after " + (i + 1) + " requests", result.startsWith("bar"));
        }
    }
    private String sendRequest(WebClient webClient, int sequence, boolean poison) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        final String path = getPath("getAndSet", sequence, poison);
        return webClient.getPage(path).getWebResponse().getContentAsString().trim();
    }
    private String getPath(String test, int sequence, boolean poison) {
        String path = contextPath + "/servlet?action=" + test + "&sequence=" + sequence;
        if (poison) {
            path += "&poison=true";
        }
        return path;
    }
}
