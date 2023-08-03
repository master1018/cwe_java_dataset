
package com.redhat.rhn.frontend.nav;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringEscapeUtils;
import com.redhat.rhn.frontend.html.HtmlTag;
public class DialognavRenderer extends Renderable {
    private final StringBuffer titleBuf;
    public DialognavRenderer() {
        titleBuf = new StringBuffer();
    }
    @Override
    public void preNav(StringBuffer sb) {
        HtmlTag div = new HtmlTag("div");
        div.setAttribute("class", "spacewalk-content-nav");
        sb.append(div.renderOpenTag());
    }
    @Override
    public void preNavLevel(StringBuffer sb, int depth) {
        if (!canRender(null, depth)) {
            return;
        }
        HtmlTag ul = new HtmlTag("ul");
        if (depth == 0) {
            ul.setAttribute("class", "nav nav-tabs");
        }
        else {
            ul.setAttribute("class", "nav nav-tabs nav-tabs-pf");
        }
        sb.append(ul.renderOpenTag());
    }
    @Override
    public void preNavNode(StringBuffer sb, int depth) {
    }
    @Override
    public void navNodeActive(StringBuffer sb,
                              NavNode node,
                              NavTreeIndex treeIndex,
                              Map parameters,
                              int depth) {
        if (!canRender(node, depth)) {
            return;
        }
        titleBuf.append(" - " + node.getName());
        renderNode(sb, node, treeIndex, parameters,
                   "active");
    }
    @Override
    public void navNodeInactive(StringBuffer sb,
                                NavNode node,
                                NavTreeIndex treeIndex,
                                Map parameters,
                                int depth) {
        if (!canRender(node, depth)) {
            return;
        }
        renderNode(sb, node, treeIndex, parameters, "");
    }
    private void renderNode(StringBuffer sb, NavNode node,
                            NavTreeIndex treeIndex, Map parameters,
                            String cssClass) {
        HtmlTag li = new HtmlTag("li");
        if (!cssClass.equals("")) {
            li.setAttribute("class", cssClass);
        }
        String href = node.getPrimaryURL();
        String allowedFormVars = treeIndex.getTree().getFormvar();
        if (allowedFormVars != null) {
            StringBuilder formVars;
            if (href.indexOf('?') == -1) {
                formVars = new StringBuilder("?");
            }
            else {
                formVars = new StringBuilder("&");
            }
            StringTokenizer st = new StringTokenizer(allowedFormVars);
            while (st.hasMoreTokens()) {
                if (formVars.length() > 1) {
                    formVars.append("&amp;");
                }
                String currentVar = st.nextToken();
                String[] values = (String[])parameters.get(currentVar);
                if (values != null) {
                    formVars.append(currentVar + "=" +
                             StringEscapeUtils.escapeHtml(values[0]));
                }
            }
            href += formVars.toString();
        }
        li.addBody(aHref(href, node.getName(), node.getTarget()));
        sb.append(li.render());
        sb.append("\n");
    }
    @Override
    public void postNavNode(StringBuffer sb, int depth) {
    }
    @Override
    public void postNavLevel(StringBuffer sb, int depth) {
        if (!canRender(null, depth)) {
            return;
        }
        HtmlTag ul = new HtmlTag("ul");
        sb.append(ul.renderCloseTag());
        sb.append("\n");
    }
    @Override
    public void postNav(StringBuffer sb) {
        HtmlTag div = new HtmlTag("div");
        sb.append(div.renderCloseTag());
        sb.append("\n");
    }
    @Override
    public boolean nodeRenderInline(int depth) {
        return false;
    }
    private static String aHref(String url, String text, String target) {
        HtmlTag a = new HtmlTag("a");
        if (target != null && !target.equals("")) {
            a.setAttribute("target", target);
        }
        a.setAttribute("href", url);
        a.addBody(text);
        return a.render();
    }
}
