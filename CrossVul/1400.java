
package com.redhat.rhn.frontend.action.groups;
import com.redhat.rhn.domain.server.ManagedServerGroup;
import com.redhat.rhn.frontend.struts.RequestContext;
import com.redhat.rhn.frontend.struts.RhnAction;
import com.redhat.rhn.frontend.struts.RhnHelper;
import com.redhat.rhn.manager.system.ServerGroupManager;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class DeleteGroupAction extends RhnAction {
    private static final String DELETED_MESSAGE_KEY = "systemgroup.delete.deleted";
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm formIn,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        RequestContext context = new RequestContext(request);
        ManagedServerGroup serverGroup = context.lookupAndBindServerGroup();
        if (context.isSubmitted()) {
            String [] params = {serverGroup.getName()};
            ServerGroupManager manager = ServerGroupManager.getInstance();
            manager.remove(context.getCurrentUser(), serverGroup);
            getStrutsDelegate().saveMessage(DELETED_MESSAGE_KEY, params, request);
            return mapping.findForward("success");
        }
        return mapping.findForward(RhnHelper.DEFAULT_FORWARD);
    }
}
