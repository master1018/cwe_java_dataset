package jenkins.security;
import hudson.model.User;
import hudson.security.ACL;
import hudson.util.Scrambler;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class ApiTokenFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;
        String authorization = req.getHeader("Authorization");
        if (authorization!=null) {
            String uidpassword = Scrambler.descramble(authorization.substring(6));
            int idx = uidpassword.indexOf(':');
            if (idx >= 0) {
                String username = uidpassword.substring(0, idx);
                String password = uidpassword.substring(idx+1);
                User u = User.get(username);
                ApiTokenProperty t = u.getProperty(ApiTokenProperty.class);
                if (t!=null && t.matchesPassword(password)) {
                    SecurityContext oldContext = ACL.impersonate(u.impersonate());
                    try {
                        request.setAttribute(ApiTokenProperty.class.getName(), u);
                        chain.doFilter(request,response);
                        return;
                    } finally {
                        SecurityContextHolder.setContext(oldContext);
                    }
                }
            }
        }
        chain.doFilter(request,response);
    }
    public void destroy() {
    }
}
