
package hudson.search;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import hudson.model.FreeStyleProject;
import hudson.model.ListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.JenkinsRule.WebClient;
import org.jvnet.hudson.test.MockFolder;
import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
public class SearchTest {
    @Rule public JenkinsRule j = new JenkinsRule();
    @Test
    public void testFailure() throws Exception {
        try {
            j.search("no-such-thing");
            fail("404 expected");
        } catch (FailingHttpStatusCodeException e) {
            assertEquals(404,e.getResponse().getStatusCode());
        }
    }
    @Issue("JENKINS-3415")
    @Test
    public void testXSS() throws Exception {
        try {
            WebClient wc = j.createWebClient();
            wc.setAlertHandler(new AlertHandler() {
                public void handleAlert(Page page, String message) {
                    throw new AssertionError();
                }
            });
            wc.search("<script>alert('script');</script>");
            fail("404 expected");
        } catch (FailingHttpStatusCodeException e) {
            assertEquals(404,e.getResponse().getStatusCode());
        }
    }
    @Test
    public void testSearchByProjectName() throws Exception {
        final String projectName = "testSearchByProjectName";
        j.createFreeStyleProject(projectName);
        Page result = j.search(projectName);
        assertNotNull(result);
        j.assertGoodStatus(result);
        String contents = result.getWebResponse().getContentAsString();
        assertTrue(contents.contains(String.format("<title>%s [Jenkins]</title>", projectName)));
    }
    @Issue("JENKINS-24433")
    @Test
    public void testSearchByProjectNameBehindAFolder() throws Exception {
        FreeStyleProject myFreeStyleProject = j.createFreeStyleProject("testSearchByProjectName");
        MockFolder myMockFolder = j.createFolder("my-folder-1");
        Page result = j.createWebClient().goTo(myMockFolder.getUrl() + "search?q="+ myFreeStyleProject.getName());
        assertNotNull(result);
        j.assertGoodStatus(result);
        URL resultUrl = result.getUrl();
        assertTrue(resultUrl.toString().equals(j.getInstance().getRootUrl() + myFreeStyleProject.getUrl()));
    }
    @Issue("JENKINS-24433")
    @Test
    public void testSearchByProjectNameInAFolder() throws Exception {
        MockFolder myMockFolder = j.createFolder("my-folder-1");
        FreeStyleProject myFreeStyleProject = myMockFolder.createProject(FreeStyleProject.class, "my-job-1");
        Page result = j.createWebClient().goTo(myMockFolder.getUrl() + "search?q=" + myFreeStyleProject.getFullName());
        assertNotNull(result);
        j.assertGoodStatus(result);
        URL resultUrl = result.getUrl();
        assertTrue(resultUrl.toString().equals(j.getInstance().getRootUrl() + myFreeStyleProject.getUrl()));
    }
    @Test
    public void testSearchByDisplayName() throws Exception {
        final String displayName = "displayName9999999";
        FreeStyleProject project = j.createFreeStyleProject("testSearchByDisplayName");
        project.setDisplayName(displayName);
        Page result = j.search(displayName);
        assertNotNull(result);
        j.assertGoodStatus(result);
        String contents = result.getWebResponse().getContentAsString();
        assertTrue(contents.contains(String.format("<title>%s [Jenkins]</title>", displayName)));
    }
    @Test
    public void testSearch2ProjectsWithSameDisplayName() throws Exception {
        final String projectName1 = "projectName1";
        final String projectName2 = "projectName2";
        final String projectName3 = "projectName3";
        final String displayName = "displayNameFoo";
        final String otherDisplayName = "otherDisplayName";
        FreeStyleProject project1 = j.createFreeStyleProject(projectName1);
        project1.setDisplayName(displayName);
        FreeStyleProject project2 = j.createFreeStyleProject(projectName2);
        project2.setDisplayName(displayName);
        FreeStyleProject project3 = j.createFreeStyleProject(projectName3);
        project3.setDisplayName(otherDisplayName);
        Page result = j.search(displayName);
        assertNotNull(result);
        j.assertGoodStatus(result);
        String contents = result.getWebResponse().getContentAsString();
        assertTrue(contents.contains(String.format("<title>%s [Jenkins]</title>", displayName)));
        assertFalse(contents.contains(otherDisplayName));
    }
    @Test
    public void testProjectNamePrecedesDisplayName() throws Exception {
        final String project1Name = "foo";
        final String project1DisplayName = "project1DisplayName";
        final String project2Name = "project2Name";
        final String project2DisplayName = project1Name;
        final String project3Name = "project3Name";
        final String project3DisplayName = "project3DisplayName";
        FreeStyleProject project1 = j.createFreeStyleProject(project1Name);
        project1.setDisplayName(project1DisplayName);
        FreeStyleProject project2 = j.createFreeStyleProject(project2Name);
        project2.setDisplayName(project2DisplayName);
        FreeStyleProject project3 = j.createFreeStyleProject(project3Name);
        project3.setDisplayName(project3DisplayName);
        Page result = j.search(project1Name);
        assertNotNull(result);
        j.assertGoodStatus(result);
        String contents = result.getWebResponse().getContentAsString();
        assertTrue(contents.contains(String.format("<title>%s [Jenkins]</title>", project1DisplayName)));
        assertFalse(contents.contains(project2Name));
        assertFalse(contents.contains(project3Name));
        assertFalse(contents.contains(project3DisplayName));
    }
    @Test
    public void testGetSuggestionsHasBothNamesAndDisplayNames() throws Exception {
        final String projectName = "project name";
        final String displayName = "display name";
        FreeStyleProject project1 = j.createFreeStyleProject(projectName);
        project1.setDisplayName(displayName);
        WebClient wc = j.createWebClient();
        Page result = wc.goTo("search/suggest?query=name", "application/json");
        assertNotNull(result);
        j.assertGoodStatus(result);
        String content = result.getWebResponse().getContentAsString();
        System.out.println(content);
        JSONObject jsonContent = (JSONObject)JSONSerializer.toJSON(content);
        assertNotNull(jsonContent);
        JSONArray jsonArray = jsonContent.getJSONArray("suggestions");
        assertNotNull(jsonArray);
        assertEquals(2, jsonArray.size());
        boolean foundProjectName = false;
        boolean foundDispayName = false;
        for(Object suggestion : jsonArray) {
            JSONObject jsonSuggestion = (JSONObject)suggestion;
            String name = (String)jsonSuggestion.get("name");
            if(projectName.equals(name)) {
                foundProjectName = true;
            }
            else if(displayName.equals(name)) {
                foundDispayName = true;
            }
        }
        assertTrue(foundProjectName);
        assertTrue(foundDispayName);
    }
    @Issue("JENKINS-24433")
    @Test
    public void testProjectNameBehindAFolderDisplayName() throws Exception {
        final String projectName1 = "job-1";
        final String displayName1 = "job-1 display";
        final String projectName2 = "job-2";
        final String displayName2 = "job-2 display";
        FreeStyleProject project1 = j.createFreeStyleProject(projectName1);
        project1.setDisplayName(displayName1);
        MockFolder myMockFolder = j.createFolder("my-folder-1");
        FreeStyleProject project2 = myMockFolder.createProject(FreeStyleProject.class, projectName2);
        project2.setDisplayName(displayName2);
        WebClient wc = j.createWebClient();
        Page result = wc.goTo(myMockFolder.getUrl() + "search/suggest?query=" + projectName1, "application/json");
        assertNotNull(result);
        j.assertGoodStatus(result);
        String content = result.getWebResponse().getContentAsString();
        JSONObject jsonContent = (JSONObject)JSONSerializer.toJSON(content);
        assertNotNull(jsonContent);
        JSONArray jsonArray = jsonContent.getJSONArray("suggestions");
        assertNotNull(jsonArray);
        assertEquals(2, jsonArray.size());
        boolean foundDisplayName = false;
        for(Object suggestion : jsonArray) {
            JSONObject jsonSuggestion = (JSONObject)suggestion;
            String name = (String)jsonSuggestion.get("name");
            if(projectName1.equals(name)) {
                foundDisplayName = true;
            }
        }
        assertTrue(foundDisplayName);
    }
    @Issue("JENKINS-24433")
    @Test
    public void testProjectNameInAFolderDisplayName() throws Exception {
        final String projectName1 = "job-1";
        final String displayName1 = "job-1 display";
        final String projectName2 = "job-2";
        final String displayName2 = "my-folder-1 job-2";
        FreeStyleProject project1 = j.createFreeStyleProject(projectName1);
        project1.setDisplayName(displayName1);
        MockFolder myMockFolder = j.createFolder("my-folder-1");
        FreeStyleProject project2 = myMockFolder.createProject(FreeStyleProject.class, projectName2);
        project2.setDisplayName(displayName2);
        WebClient wc = j.createWebClient();
        Page result = wc.goTo(myMockFolder.getUrl() + "search/suggest?query=" + projectName2, "application/json");
        assertNotNull(result);
        j.assertGoodStatus(result);
        String content = result.getWebResponse().getContentAsString();
        JSONObject jsonContent = (JSONObject)JSONSerializer.toJSON(content);
        assertNotNull(jsonContent);
        JSONArray jsonArray = jsonContent.getJSONArray("suggestions");
        assertNotNull(jsonArray);
        assertEquals(1, jsonArray.size());
        boolean foundDisplayName = false;
        for(Object suggestion : jsonArray) {
            JSONObject jsonSuggestion = (JSONObject)suggestion;
            String name = (String)jsonSuggestion.get("name");
            if(displayName2.equals(name)) {
                foundDisplayName = true;
            }
        }
        assertTrue(foundDisplayName);
    }
    @Issue("JENKINS-13148")
    @Test
    public void testDisabledJobShouldBeSearchable() throws Exception {
        FreeStyleProject p = j.createFreeStyleProject("foo-bar");
        assertTrue(suggest(j.jenkins.getSearchIndex(), "foo").contains(p));
        p.disable();
        assertTrue(suggest(j.jenkins.getSearchIndex(), "foo").contains(p));
    }
    @Issue("JENKINS-13148")
    @Test
    public void testCompletionOutsideView() throws Exception {
        FreeStyleProject p = j.createFreeStyleProject("foo-bar");
        ListView v = new ListView("empty1",j.jenkins);
        ListView w = new ListView("empty2",j.jenkins);
        j.jenkins.addView(v);
        j.jenkins.addView(w);
        j.jenkins.setPrimaryView(w);
        assertFalse(v.contains(p));
        assertFalse(w.contains(p));
        assertFalse(j.jenkins.getPrimaryView().contains(p));
        assertTrue(suggest(j.jenkins.getSearchIndex(),"foo").contains(p));
    }
    @Test
    public void testSearchWithinFolders() throws Exception {
        MockFolder folder1 = j.createFolder("folder1");
        FreeStyleProject p1 = folder1.createProject(FreeStyleProject.class, "myjob");
        MockFolder folder2 = j.createFolder("folder2");
        FreeStyleProject p2 = folder2.createProject(FreeStyleProject.class, "myjob");
        List<SearchItem> suggest = suggest(j.jenkins.getSearchIndex(), "myjob");
        assertTrue(suggest.contains(p1));
        assertTrue(suggest.contains(p2));
    }
    private List<SearchItem> suggest(SearchIndex index, String term) {
        List<SearchItem> result = new ArrayList<SearchItem>();
        index.suggest(term, result);
        return result;
    }
}
