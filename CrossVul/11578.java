
package org.jboss.weld.tests.contexts.cache;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.weld.context.beanstore.ConversationNamingScheme;
@WebServlet(value = "/servlet", asyncSupported = true)
public class SimpleServlet extends HttpServlet {
    @Inject
    private ConversationScopedBean bean;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String sequence = req.getParameter("sequence");
        String poison = req.getParameter("poison");
        if ("getAndSet".equals(action)) {
            String value = bean.getAndSet("bar" + sequence);
            resp.getWriter().println(value);
            if (poison != null) {
                req.removeAttribute(ConversationNamingScheme.PARAMETER_NAME);
            }
        } else {
            throw new IllegalArgumentException(action);
        }
    }
}
