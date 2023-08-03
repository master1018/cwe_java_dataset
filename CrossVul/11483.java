package org.jolokia.restrictor.policy;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.w3c.dom.*;
public class CorsChecker extends AbstractChecker<String> {
    private boolean strictChecking = false;
    private List<Pattern> patterns;
    public CorsChecker(Document pDoc) {
        NodeList corsNodes = pDoc.getElementsByTagName("cors");
        if (corsNodes.getLength() > 0) {
            patterns = new ArrayList<Pattern>();
            for (int i = 0; i < corsNodes.getLength(); i++) {
                Node corsNode = corsNodes.item(i);
                NodeList nodes = corsNode.getChildNodes();
                for (int j = 0;j <nodes.getLength();j++) {
                    Node node = nodes.item(j);
                    if (node.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    assertNodeName(node,"allow-origin","strict-checking");
                    if (node.getNodeName().equals("allow-origin")) {
                        String p = node.getTextContent().trim().toLowerCase();
                        p = Pattern.quote(p).replace("*", "\\E.*\\Q");
                        patterns.add(Pattern.compile("^" + p + "$"));
                    } else if (node.getNodeName().equals("strict-checking")) {
                        strictChecking = true;
                    }
                }
            }
        }
    }
    @Override
    public boolean check(String pArg) {
        return check(pArg,false);
    }
    public boolean check(String pOrigin, boolean pIsStrictCheck) {
        if (pIsStrictCheck && !strictChecking) {
            return true;
        }
        if (patterns == null || patterns.size() == 0) {
            return true;
        }
        for (Pattern pattern : patterns) {
            if (pattern.matcher(pOrigin).matches()) {
                return true;
            }
        }
        return false;
    }
}
