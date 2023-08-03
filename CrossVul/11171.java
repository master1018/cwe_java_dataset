
package hudson.security;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import hudson.Extension;
import hudson.Util;
import hudson.diagnosis.OldDataMonitor;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import hudson.model.ManagementLink;
import hudson.model.ModelObject;
import hudson.model.User;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import hudson.security.FederatedLoginService.FederatedIdentity;
import hudson.security.captcha.CaptchaSupport;
import hudson.util.PluginServletFilter;
import hudson.util.Protector;
import hudson.util.Scrambler;
import hudson.util.XStream2;
import net.sf.json.JSONObject;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.providers.encoding.ShaPasswordEncoder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.tools.ant.taskdefs.email.Mailer;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.ForwardToView;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
public class HudsonPrivateSecurityRealm extends AbstractPasswordBasedSecurityRealm implements ModelObject, AccessControlled {
    private final boolean disableSignup;
    private final boolean enableCaptcha;
    @Deprecated
    public HudsonPrivateSecurityRealm(boolean allowsSignup) {
        this(allowsSignup, false, (CaptchaSupport) null);
    }
    @DataBoundConstructor
    public HudsonPrivateSecurityRealm(boolean allowsSignup, boolean enableCaptcha, CaptchaSupport captchaSupport) {
        this.disableSignup = !allowsSignup;
        this.enableCaptcha = enableCaptcha;
        setCaptchaSupport(captchaSupport);
        if(!allowsSignup && !hasSomeUser()) {
            try {
                PluginServletFilter.addFilter(CREATE_FIRST_USER_FILTER);
            } catch (ServletException e) {
                throw new AssertionError(e); 
            }
        }
    }
    @Override
    public boolean allowsSignup() {
        return !disableSignup;
    }
    public boolean isEnableCaptcha() {
        return enableCaptcha;
    }
    private static boolean hasSomeUser() {
        for (User u : User.getAll())
            if(u.getProperty(Details.class)!=null)
                return true;
        return false;
    }
    @Override
    public GroupDetails loadGroupByGroupname(String groupname) throws UsernameNotFoundException, DataAccessException {
        throw new UsernameNotFoundException(groupname);
    }
    @Override
    public Details loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User u = User.get(username,false);
        Details p = u!=null ? u.getProperty(Details.class) : null;
        if(p==null)
            throw new UsernameNotFoundException("Password is not set: "+username);
        if(p.getUser()==null)
            throw new AssertionError();
        return p;
    }
    @Override
    protected Details authenticate(String username, String password) throws AuthenticationException {
        Details u = loadUserByUsername(username);
        if (!u.isPasswordCorrect(password)) {
            String message;
            try {
                message = ResourceBundle.getBundle("org.acegisecurity.messages").getString("AbstractUserDetailsAuthenticationProvider.badCredentials");
            } catch (MissingResourceException x) {
                message = "Bad credentials";
            }
            throw new BadCredentialsException(message);
        }
        return u;
    }
    @Override
    public HttpResponse commenceSignup(final FederatedIdentity identity) {
        Stapler.getCurrentRequest().getSession().setAttribute(FEDERATED_IDENTITY_SESSION_KEY,identity);
        return new ForwardToView(this,"signupWithFederatedIdentity.jelly") {
            @Override
            public void generateResponse(StaplerRequest req, StaplerResponse rsp, Object node) throws IOException, ServletException {
                SignupInfo si = new SignupInfo(identity);
                si.errorMessage = Messages.HudsonPrivateSecurityRealm_WouldYouLikeToSignUp(identity.getPronoun(),identity.getIdentifier());
                req.setAttribute("data", si);
                super.generateResponse(req, rsp, node);
            }
        };
    }
    public User doCreateAccountWithFederatedIdentity(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        User u = _doCreateAccount(req,rsp,"signupWithFederatedIdentity.jelly");
        if (u!=null)
            ((FederatedIdentity)req.getSession().getAttribute(FEDERATED_IDENTITY_SESSION_KEY)).addTo(u);
        return u;
    }
    private static final String FEDERATED_IDENTITY_SESSION_KEY = HudsonPrivateSecurityRealm.class.getName()+".federatedIdentity";
    public User doCreateAccount(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        return _doCreateAccount(req, rsp, "signup.jelly");
    }
    private User _doCreateAccount(StaplerRequest req, StaplerResponse rsp, String formView) throws ServletException, IOException {
        if(!allowsSignup())
            throw HttpResponses.error(SC_UNAUTHORIZED,new Exception("User sign up is prohibited"));
        boolean firstUser = !hasSomeUser();
        User u = createAccount(req, rsp, enableCaptcha, formView);
        if(u!=null) {
            if(firstUser)
                tryToMakeAdmin(u);  
            loginAndTakeBack(req, rsp, u);
        }
        return u;
    }
    private void loginAndTakeBack(StaplerRequest req, StaplerResponse rsp, User u) throws ServletException, IOException {
        Authentication a = new UsernamePasswordAuthenticationToken(u.getId(),req.getParameter("password1"));
        a = this.getSecurityComponents().manager.authenticate(a);
        SecurityContextHolder.getContext().setAuthentication(a);
        req.getView(this,"success.jelly").forward(req,rsp);
    }
    public void doCreateAccountByAdmin(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        checkPermission(Jenkins.ADMINISTER);
        if(createAccount(req, rsp, false, "addUser.jelly")!=null) {
            rsp.sendRedirect(".");  
        }
    }
    public void doCreateFirstAccount(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        if(hasSomeUser()) {
            rsp.sendError(SC_UNAUTHORIZED,"First user was already created");
            return;
        }
        User u = createAccount(req, rsp, false, "firstUser.jelly");
        if (u!=null) {
            tryToMakeAdmin(u);
            loginAndTakeBack(req, rsp, u);
        }
    }
    private void tryToMakeAdmin(User u) {
        AuthorizationStrategy as = Jenkins.getInstance().getAuthorizationStrategy();
        if (as instanceof GlobalMatrixAuthorizationStrategy) {
            GlobalMatrixAuthorizationStrategy ma = (GlobalMatrixAuthorizationStrategy) as;
            ma.add(Jenkins.ADMINISTER,u.getId());
        }
    }
    private User createAccount(StaplerRequest req, StaplerResponse rsp, boolean selfRegistration, String formView) throws ServletException, IOException {
        SignupInfo si = new SignupInfo(req);
        if(selfRegistration && !validateCaptcha(si.captcha))
            si.errorMessage = Messages.HudsonPrivateSecurityRealm_CreateAccount_TextNotMatchWordInImage();
        if(si.password1 != null && !si.password1.equals(si.password2))
            si.errorMessage = Messages.HudsonPrivateSecurityRealm_CreateAccount_PasswordNotMatch();
        if(!(si.password1 != null && si.password1.length() != 0))
            si.errorMessage = Messages.HudsonPrivateSecurityRealm_CreateAccount_PasswordRequired();
        if(si.username==null || si.username.length()==0)
            si.errorMessage = Messages.HudsonPrivateSecurityRealm_CreateAccount_UserNameRequired();
        else {
            User user = User.get(si.username, false);
            if (null != user)
                si.errorMessage = Messages.HudsonPrivateSecurityRealm_CreateAccount_UserNameAlreadyTaken();
        }
        if(si.fullname==null || si.fullname.length()==0)
            si.fullname = si.username;
        if(si.email==null || !si.email.contains("@"))
            si.errorMessage = Messages.HudsonPrivateSecurityRealm_CreateAccount_InvalidEmailAddress();
        if(si.errorMessage!=null) {
            req.setAttribute("data",si);
            req.getView(this, formView).forward(req,rsp);
            return null;
        }
        User user = createAccount(si.username,si.password1);
        user.setFullName(si.fullname);
        try {
            Class<?> up = Jenkins.getInstance().pluginManager.uberClassLoader.loadClass("hudson.tasks.Mailer$UserProperty");
            Constructor<?> c = up.getDeclaredConstructor(String.class);
            user.addProperty((UserProperty)c.newInstance(si.email));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to set the e-mail address",e);
        }
        user.save();
        return user;
    }
    public User createAccount(String userName, String password) throws IOException {
        User user = User.get(userName);
        user.addProperty(Details.fromPlainPassword(password));
        return user;
    }
    public String getDisplayName() {
        return "User Database";
    }
    public ACL getACL() {
        return Jenkins.getInstance().getACL();
    }
    public void checkPermission(Permission permission) {
        Jenkins.getInstance().checkPermission(permission);
    }
    public boolean hasPermission(Permission permission) {
        return Jenkins.getInstance().hasPermission(permission);
    }
    public List<User> getAllUsers() {
        List<User> r = new ArrayList<User>();
        for (User u : User.getAll()) {
            if(u.getProperty(Details.class)!=null)
                r.add(u);
        }
        Collections.sort(r);
        return r;
    }
    public User getUser(String id) {
        return User.get(id);
    }
    private static final GrantedAuthority[] TEST_AUTHORITY = {AUTHENTICATED_AUTHORITY};
    public static final class SignupInfo {
        public String username,password1,password2,fullname,email,captcha;
        public String errorMessage;
        public SignupInfo() {
        }
        public SignupInfo(StaplerRequest req) {
            req.bindParameters(this);
        }
        public SignupInfo(FederatedIdentity i) {
            this.username = i.getNickname();
            this.fullname = i.getFullName();
            this.email = i.getEmailAddress();
        }
    }
    public static final class Details extends UserProperty implements InvalidatableUserDetails {
        private  String passwordHash;
        private transient String password;
        private Details(String passwordHash) {
            this.passwordHash = passwordHash;
        }
        static Details fromHashedPassword(String hashed) {
            return new Details(hashed);
        }
        static Details fromPlainPassword(String rawPassword) {
            return new Details(PASSWORD_ENCODER.encodePassword(rawPassword,null));
        }
        public GrantedAuthority[] getAuthorities() {
            return TEST_AUTHORITY;
        }
        public String getPassword() {
            return passwordHash;
        }
        public boolean isPasswordCorrect(String candidate) {
            return PASSWORD_ENCODER.isPasswordValid(getPassword(),candidate,null);
        }
        public String getProtectedPassword() {
            return Protector.protect(Stapler.getCurrentRequest().getSession().getId()+':'+getPassword());
        }
        public String getUsername() {
            return user.getId();
        }
         User getUser() {
            return user;
        }
        public boolean isAccountNonExpired() {
            return true;
        }
        public boolean isAccountNonLocked() {
            return true;
        }
        public boolean isCredentialsNonExpired() {
            return true;
        }
        public boolean isEnabled() {
            return true;
        }
        public boolean isInvalid() {
            return user==null;
        }
        public static class ConverterImpl extends XStream2.PassthruConverter<Details> {
            public ConverterImpl(XStream2 xstream) { super(xstream); }
            @Override protected void callback(Details d, UnmarshallingContext context) {
                if (d.password!=null && d.passwordHash==null) {
                    d.passwordHash = PASSWORD_ENCODER.encodePassword(Scrambler.descramble(d.password),null);
                    OldDataMonitor.report(context, "1.283");
                }
            }
        }
        @Extension
        public static final class DescriptorImpl extends UserPropertyDescriptor {
            public String getDisplayName() {
                if(isEnabled())
                    return Messages.HudsonPrivateSecurityRealm_Details_DisplayName();
                else
                    return null;
            }
            @Override
            public Details newInstance(StaplerRequest req, JSONObject formData) throws FormException {
                String pwd = Util.fixEmpty(req.getParameter("user.password"));
                String pwd2= Util.fixEmpty(req.getParameter("user.password2"));
                if(!Util.fixNull(pwd).equals(Util.fixNull(pwd2)))
                    throw new FormException("Please confirm the password by typing it twice","user.password2");
                String data = Protector.unprotect(pwd);
                if(data!=null) {
                    String prefix = Stapler.getCurrentRequest().getSession().getId() + ':';
                    if(data.startsWith(prefix))
                        return Details.fromHashedPassword(data.substring(prefix.length()));
                }
                return Details.fromPlainPassword(Util.fixNull(pwd));
            }
            @Override
            public boolean isEnabled() {
                return Jenkins.getInstance().getSecurityRealm() instanceof HudsonPrivateSecurityRealm;
            }
            public UserProperty newInstance(User user) {
                return null;
            }
        }
    }
    @Extension
    public static final class ManageUserLinks extends ManagementLink {
        public String getIconFileName() {
            if(Jenkins.getInstance().getSecurityRealm() instanceof HudsonPrivateSecurityRealm)
                return "user.png";
            else
                return null;    
        }
        public String getUrlName() {
            return "securityRealm/";
        }
        public String getDisplayName() {
            return Messages.HudsonPrivateSecurityRealm_ManageUserLinks_DisplayName();
        }
        @Override
        public String getDescription() {
            return Messages.HudsonPrivateSecurityRealm_ManageUserLinks_Description();
        }
    }
     static final PasswordEncoder CLASSIC = new PasswordEncoder() {
        private final PasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);
        public String encodePassword(String rawPass, Object _) throws DataAccessException {
            return hash(rawPass);
        }
        public boolean isPasswordValid(String encPass, String rawPass, Object _) throws DataAccessException {
            int i = encPass.indexOf(':');
            if(i<0) return false;
            String salt = encPass.substring(0,i);
            return encPass.substring(i+1).equals(passwordEncoder.encodePassword(rawPass,salt));
        }
        private String hash(String password) {
            String salt = generateSalt();
            return salt+':'+passwordEncoder.encodePassword(password,salt);
        }
        private String generateSalt() {
            StringBuilder buf = new StringBuilder();
            SecureRandom sr = new SecureRandom();
            for( int i=0; i<6; i++ ) {
                boolean upper = sr.nextBoolean();
                char ch = (char)(sr.nextInt(26) + 'a');
                if(upper)   ch=Character.toUpperCase(ch);
                buf.append(ch);
            }
            return buf.toString();
        }
    };
    private static final PasswordEncoder JBCRYPT_ENCODER = new PasswordEncoder() {
        public String encodePassword(String rawPass, Object _) throws DataAccessException {
            return BCrypt.hashpw(rawPass,BCrypt.gensalt());
        }
        public boolean isPasswordValid(String encPass, String rawPass, Object _) throws DataAccessException {
            return BCrypt.checkpw(rawPass,encPass);
        }
    };
    public static final PasswordEncoder PASSWORD_ENCODER = new PasswordEncoder() {
        public String encodePassword(String rawPass, Object salt) throws DataAccessException {
            return JBCRYPT_HEADER+JBCRYPT_ENCODER.encodePassword(rawPass,salt);
        }
        public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {
            if (encPass.startsWith(JBCRYPT_HEADER))
                return JBCRYPT_ENCODER.isPasswordValid(encPass.substring(JBCRYPT_HEADER.length()),rawPass,salt);
            else
                return CLASSIC.isPasswordValid(encPass,rawPass,salt);
        }
        private static final String JBCRYPT_HEADER = "#jbcrypt:";
    };
    @Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
        public String getDisplayName() {
            return Messages.HudsonPrivateSecurityRealm_DisplayName();
        }
        @Override
        public String getHelpFile() {
            return "/help/security/private-realm.html"; 
        }
    }
    private static final Filter CREATE_FIRST_USER_FILTER = new Filter() {
        public void init(FilterConfig config) throws ServletException {
        }
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            if(req.getRequestURI().equals(req.getContextPath()+"/")) {
                if (needsToCreateFirstUser()) {
                    ((HttpServletResponse)response).sendRedirect("securityRealm/firstUser");
                } else {
                    PluginServletFilter.removeFilter(this);
                    chain.doFilter(request,response);
                }
            } else
                chain.doFilter(request,response);
        }
        private boolean needsToCreateFirstUser() {
            return !hasSomeUser()
                && Jenkins.getInstance().getSecurityRealm() instanceof HudsonPrivateSecurityRealm;
        }
        public void destroy() {
        }
    };
    private static final Logger LOGGER = Logger.getLogger(HudsonPrivateSecurityRealm.class.getName());
}
