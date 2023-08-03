
package org.jasig.cas.util;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.LdapEncoder;
public final class LdapUtils {
    private static final Logger logger = LoggerFactory.getLogger(LdapUtils.class);
    private LdapUtils() {
    }
    public static String getFilterWithValues(final String filter,
        final String userName) {
        final Map<String, String> properties = new HashMap<String, String>();
        final String[] userDomain;
        String newFilter = filter;
        properties.put("%u", userName);
        userDomain = userName.split("@");
        properties.put("%U", userDomain[0]);
        if (userDomain.length > 1) {
            properties.put("%d", userDomain[1]);
            final String[] dcArray = userDomain[1].split("\\.");
            for (int i = 0; i < dcArray.length; i++) {
                properties.put("%" + (i + 1), dcArray[dcArray.length
                    - 1 - i]);
            }
        }
        for (final String key : properties.keySet()) {
            final String value = LdapEncoder.filterEncode(properties.get(key));
            newFilter = newFilter.replaceAll(key, Matcher.quoteReplacement(value));
        }
        return newFilter;
    }
    public static void closeContext(final DirContext context) {
        if (context != null) {
            try {
                context.close();
            } catch (NamingException ex) {
                logger.warn("Could not close context", ex);
            }
        }
    }
}
