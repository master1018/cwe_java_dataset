
package hudson.cli;
import hudson.model.ModifiableItemGroup;
import hudson.model.TopLevelItem;
import jenkins.model.Jenkins;
import hudson.Extension;
import hudson.model.Item;
import jenkins.model.ModifiableTopLevelItemGroup;
import org.kohsuke.args4j.Argument;
@Extension
public class CreateJobCommand extends CLICommand {
    @Override
    public String getShortDescription() {
        return Messages.CreateJobCommand_ShortDescription();
    }
    @Argument(metaVar="NAME",usage="Name of the job to create",required=true)
    public String name;
    protected int run() throws Exception {
        Jenkins h = Jenkins.getInstance();
        h.checkPermission(Item.CREATE);
        if (h.getItemByFullName(name)!=null) {
            stderr.println("Job '"+name+"' already exists");
            return -1;
        }
        ModifiableTopLevelItemGroup ig = h;
        int i = name.lastIndexOf('/');
        if (i > 0) {
            String group = name.substring(0, i);
            Item item = h.getItemByFullName(group);
            if (item == null) {
                throw new IllegalArgumentException("Unknown ItemGroup " + group);
            }
            if (item instanceof ModifiableTopLevelItemGroup) {
                ig = (ModifiableTopLevelItemGroup) item;
            } else {
                throw new IllegalArgumentException("Can't create job from CLI in " + group);
            }
            name = name.substring(i + 1);
        }
        ig.createProjectFromXML(name, stdin);
        return 0;
    }
}
