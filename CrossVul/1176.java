
package org.jboss.security.negotiation;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ReferralException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.CompositeName;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.jboss.security.SimpleGroup;
import org.jboss.security.negotiation.common.CommonLoginModule;
import org.jboss.security.negotiation.prototype.DecodeAction;
import org.jboss.security.vault.SecurityVaultUtil;
import org.jboss.security.vault.SecurityVaultException;
public class AdvancedLdapLoginModule extends CommonLoginModule
{
   private static final String BIND_AUTHENTICATION = "bindAuthentication";
   private static final String BIND_DN = "bindDN";
   private static final String BIND_CREDENTIAL = "bindCredential";
   private static final String SECURITY_DOMAIN = "jaasSecurityDomain";
   private static final String BASE_CTX_DN = "baseCtxDN";
   private static final String BASE_FILTER = "baseFilter";
   private static final String SEARCH_TIME_LIMIT = "searchTimeLimit";
   private static final String ROLES_CTS_DN = "rolesCtxDN";
   private static final String ROLE_FILTER = "roleFilter";
   private static final String RECURSE_ROLES = "recurseRoles";
   private static final String ROLE_ATTRIBUTE_ID = "roleAttributeID";
   private static final String ROLE_ATTRIBUTE_IS_DN = "roleAttributeIsDN";
   private static final String ROLE_NAME_ATTRIBUTE_ID = "roleNameAttributeID";
   private static final String ROLE_SEARCH_SCOPE = "searchScope";
   private static final String REFERRAL_USER_ATTRIBUTE_ID_TO_CHECK = "referralUserAttributeIDToCheck";
   private static final String ALLOW_EMPTY_PASSWORD = "allowEmptyPassword";
   private static final String AUTH_TYPE_GSSAPI = "GSSAPI";
   private static final String AUTH_TYPE_SIMPLE = "simple";
   private static final String DEFAULT_LDAP_CTX_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
   private static final String DEFAULT_URL = "ldap:
   private static final String DEFAULT_SSL_URL = "ldap:
   private static final String PROTOCOL_SSL = "SSL";
   private static final String OBJECT_SCOPE = "OBJECT_SCOPE";
   private static final String ONELEVEL_SCOPE = "ONELEVEL_SCOPE";
   private static final String SUBTREE_SCOPE = "SUBTREE_SCOPE";
   private static final String[] ALL_VALID_OPTIONS =
   {
      BIND_AUTHENTICATION,BIND_DN,BIND_CREDENTIAL,SECURITY_DOMAIN,
      BASE_CTX_DN,BASE_FILTER,SEARCH_TIME_LIMIT,
      ROLES_CTS_DN,ROLE_FILTER,RECURSE_ROLES,ROLE_ATTRIBUTE_ID,ROLE_ATTRIBUTE_IS_DN,ROLE_NAME_ATTRIBUTE_ID,ROLE_SEARCH_SCOPE,
      ALLOW_EMPTY_PASSWORD,REFERRAL_USER_ATTRIBUTE_ID_TO_CHECK,
      Context.INITIAL_CONTEXT_FACTORY,
      Context.OBJECT_FACTORIES,
      Context.STATE_FACTORIES,
      Context.URL_PKG_PREFIXES,
      Context.PROVIDER_URL,
      Context.DNS_URL,
      Context.AUTHORITATIVE,
      Context.BATCHSIZE,
      Context.REFERRAL,
      Context.SECURITY_PROTOCOL,
      Context.SECURITY_AUTHENTICATION,
      Context.SECURITY_PRINCIPAL,
      Context.SECURITY_CREDENTIALS,
      Context.LANGUAGE,
      Context.APPLET
   };
   protected String bindAuthentication;
   protected String bindDn;
   protected String bindCredential;
   protected String jaasSecurityDomain;
   protected String baseCtxDN;
   protected String baseFilter;
   protected int searchTimeLimit = 10000;
   protected SearchControls userSearchControls;
   protected String rolesCtxDN;
   protected String roleFilter;
   protected boolean recurseRoles;
   protected SearchControls roleSearchControls;
   protected String roleAttributeID;
   protected boolean roleAttributeIsDN;
   protected String roleNameAttributeID;
   protected String referralUserAttributeIDToCheck = null;
   protected boolean allowEmptyPassword;
   private String referralUserDNToCheck;
   private SimpleGroup userRoles = new SimpleGroup("Roles");
   private Set<String> processedRoleDNs = new HashSet<String>();
   private boolean trace;
   @Override
   public void initialize(Subject subject, CallbackHandler handler, Map sharedState, Map options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, handler, sharedState, options);
      trace = log.isTraceEnabled();
      bindAuthentication = (String) options.get(BIND_AUTHENTICATION);
      bindDn = (String) options.get(BIND_DN);
      bindCredential = (String) options.get(BIND_CREDENTIAL);
      try
      {
        if(bindCredential != null && SecurityVaultUtil.isVaultFormat(bindCredential))
        {
          bindCredential = SecurityVaultUtil.getValueAsString(bindCredential);
        }
      }
      catch (SecurityVaultException e)
      {
        log.warn("Unable to obtain bindCredentials from Vault: ", e);
      }
      jaasSecurityDomain = (String) options.get(SECURITY_DOMAIN);
      baseCtxDN = (String) options.get(BASE_CTX_DN);
      baseFilter = (String) options.get(BASE_FILTER);
      String temp = (String) options.get(SEARCH_TIME_LIMIT);
      if (temp != null)
      {
         try
         {
            searchTimeLimit = Integer.parseInt(temp);
         }
         catch (NumberFormatException e)
         {
            log.warn("Failed to parse: " + temp + ", using searchTimeLimit=" + searchTimeLimit);
         }
      }
      userSearchControls = new SearchControls();
      userSearchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      userSearchControls.setReturningAttributes(new String[0]);
      userSearchControls.setTimeLimit(searchTimeLimit);
      rolesCtxDN = (String) options.get(ROLES_CTS_DN);
      roleFilter = (String) options.get(ROLE_FILTER);
      referralUserAttributeIDToCheck = (String) options.get(REFERRAL_USER_ATTRIBUTE_ID_TO_CHECK);
      temp = (String) options.get(RECURSE_ROLES);
      recurseRoles = Boolean.parseBoolean(temp);
      int searchScope = SearchControls.SUBTREE_SCOPE;
      temp = (String) options.get(ROLE_SEARCH_SCOPE);
      if (OBJECT_SCOPE.equalsIgnoreCase(temp))
      {
         searchScope = SearchControls.OBJECT_SCOPE;
      }
      else if (ONELEVEL_SCOPE.equalsIgnoreCase(temp))
      {
         searchScope = SearchControls.ONELEVEL_SCOPE;
      }
      if (SUBTREE_SCOPE.equalsIgnoreCase(temp))
      {
         searchScope = SearchControls.SUBTREE_SCOPE;
      }
      roleSearchControls = new SearchControls();
      roleSearchControls.setSearchScope(searchScope);
      roleSearchControls.setTimeLimit(searchTimeLimit);
      roleAttributeID = (String) options.get(ROLE_ATTRIBUTE_ID);
      temp = (String) options.get(ROLE_ATTRIBUTE_IS_DN);
      roleAttributeIsDN = Boolean.parseBoolean(temp);
      roleNameAttributeID = (String) options.get(ROLE_NAME_ATTRIBUTE_ID);
      ArrayList<String> roleSearchAttributeList = new ArrayList<String>(3); 
      if (roleAttributeID != null) 
      {
          roleSearchAttributeList.add(roleAttributeID);
      }
      if (roleNameAttributeID != null)
      {
          roleSearchAttributeList.add(roleNameAttributeID);
      } 
      if (referralUserAttributeIDToCheck != null)
      {
          roleSearchAttributeList.add(referralUserAttributeIDToCheck);
      } 
      roleSearchControls.setReturningAttributes(roleSearchAttributeList.toArray(new String[0]));
      temp = (String) options.get(ALLOW_EMPTY_PASSWORD);
      allowEmptyPassword = Boolean.parseBoolean(temp);
   }
   @Override
   public boolean login() throws LoginException
   {
      Object result = null;
      AuthorizeAction action = new AuthorizeAction();
      if (AUTH_TYPE_GSSAPI.equals(bindAuthentication))
      {
         log.trace("Using GSSAPI to connect to LDAP");
         LoginContext lc = new LoginContext(jaasSecurityDomain);
         lc.login();
         Subject serverSubject = lc.getSubject();
         if (log.isDebugEnabled())
         {
            log.debug("Subject = " + serverSubject);
            log.debug("Logged in '" + lc + "' LoginContext");
         }
         result = Subject.doAs(serverSubject, action);
         lc.logout();
      }
      else
      {
         result = action.run();
      }
      if (result instanceof LoginException)
      {
         throw (LoginException) result;
      }
      return ((Boolean) result).booleanValue();
   }
   @Override
   protected Group[] getRoleSets() throws LoginException
   {
      Group[] roleSets =
      {userRoles};
      return roleSets;
   }
   protected Boolean innerLogin() throws LoginException
   {
      processIdentityAndCredential();
      if (trace) {
         log.trace("Identity - " + getIdentity().getName());
      }
      String bindCredential = this.bindCredential;
      if (AUTH_TYPE_GSSAPI.equals(bindAuthentication) == false)
      {
         if (jaasSecurityDomain != null && jaasSecurityDomain.length() > 0)
         {
            try
            {
               ObjectName serviceName = new ObjectName(jaasSecurityDomain);
               char[] tmp = DecodeAction.decode(bindCredential, serviceName);
               bindCredential = new String(tmp);
            }
            catch (Exception e)
            {
               LoginException le = new LoginException("Unable to decode bindCredential");
               le.initCause(e);
               throw le;
            }
         }
      }
      LdapContext searchContext = null;
      try
      {
         searchContext = constructLdapContext(null, bindDn, bindCredential, bindAuthentication);
         log.debug("Obtained LdapContext");
         String userDN = findUserDN(searchContext);
         if (referralUserAttributeIDToCheck != null)
         {
            if (isUserDnAbsolute(userDN))
            {
                referralUserDNToCheck = localUserDN(userDN);
            }
            else 
            {
               referralUserDNToCheck = userDN;
            }
         }
         if (super.loginOk == false)
         {
            authenticate(userDN);
         }
         if (super.loginOk)
         {
            rolesSearch(searchContext, userDN);
         }
      }
      finally
      {
         if (searchContext != null)
         {
            try
            {
               searchContext.close();
            }
            catch (NamingException e)
            {
               log.warn("Error closing context", e);
            }
         }
      }
      return Boolean.valueOf(super.loginOk);
   }
   private Properties constructLdapContextEnvironment(String namingProviderURL, String principalDN, Object credential, String authentication) 
   {
       Properties env = createBaseProperties();
       String factoryName = env.getProperty(Context.INITIAL_CONTEXT_FACTORY);
       if (factoryName == null)
       {
          factoryName = DEFAULT_LDAP_CTX_FACTORY;
          env.setProperty(Context.INITIAL_CONTEXT_FACTORY, factoryName);
       }
       if (authentication != null && authentication.length() > 0)
       {
          env.setProperty(Context.SECURITY_AUTHENTICATION, authentication);
       }
       else
       {
          String authType = env.getProperty(Context.SECURITY_AUTHENTICATION);
          if (authType == null)
             env.setProperty(Context.SECURITY_AUTHENTICATION, AUTH_TYPE_SIMPLE);
       }
       String providerURL = null;
       if (namingProviderURL != null)
       {
          providerURL = namingProviderURL;
       }
       else
       {
          providerURL = (String) options.get(Context.PROVIDER_URL);
       }
       String protocol = env.getProperty(Context.SECURITY_PROTOCOL);
       if (providerURL == null)
       {
          if (PROTOCOL_SSL.equals(protocol))
          {
             providerURL = DEFAULT_SSL_URL;
          }
          else
          {
             providerURL = DEFAULT_URL;
          }
       }
       env.setProperty(Context.PROVIDER_URL, providerURL);
       if (principalDN != null)
          env.setProperty(Context.SECURITY_PRINCIPAL, principalDN);
       if (credential != null)
          env.put(Context.SECURITY_CREDENTIALS, credential);
       traceLdapEnv(env);
       return env;
   }
   protected LdapContext constructLdapContext(String namingProviderURL, String dn, Object credential, String authentication)
         throws LoginException
   {
      try
      {
          Properties env = constructLdapContextEnvironment(namingProviderURL, dn, credential, authentication);
          return new InitialLdapContext(env, null);
      }
      catch (NamingException e)
      {
         LoginException le = new LoginException("Unable to create new InitialLdapContext");
         le.initCause(e);
         throw le;
      }
   }
   protected Properties createBaseProperties()
   {
      Properties env = new Properties();
      Iterator iter = options.entrySet().iterator();
      while (iter.hasNext())
      {
         Entry entry = (Entry) iter.next();
         env.put(entry.getKey(), entry.getValue());
      }
      return env;
   }
   protected String findUserDN(LdapContext ctx) throws LoginException
   {
      if (baseCtxDN == null)
      {
         return getIdentity().getName();
      }
      try
      {
         NamingEnumeration results = null;
         Object[] filterArgs =
         {getIdentity().getName()};
         LdapContext ldapCtx = ctx;
         boolean referralsLeft = true;
         SearchResult sr = null;
         while (referralsLeft) 
         {
            try 
            {
               results = ldapCtx.search(baseCtxDN, baseFilter, filterArgs, userSearchControls);
               while (results.hasMore()) 
               {
                  sr = (SearchResult) results.next();
                  break;
               }
               referralsLeft = false;
            }
            catch (ReferralException e) 
            {
               ldapCtx = (LdapContext) e.getReferralContext();
               if (results != null) 
               {
                  results.close();
               }
            }
         }
         if (sr == null)
         {
            results.close();
            throw new LoginException("Search of baseDN(" + baseCtxDN + ") found no matches");
         }
         String name = sr.getName();
         String userDN = null;
         if (sr.isRelative() == true) 
         {
            userDN = new CompositeName(name).get(0) + "," + baseCtxDN;
         }
         else
         {
            userDN = sr.getName();
         }
         results.close();
         results = null;
         if (trace) {
            log.trace("findUserDN - " + userDN);
         }
         return userDN;
      }
      catch (NamingException e)
      {
         LoginException le = new LoginException("Unable to find user DN");
         le.initCause(e);
         throw le;
      }
   }
   private void referralAuthenticate(String absoluteName, Object credential)
           throws LoginException
   {
       URI uri;
       try 
       {
           uri = new URI(absoluteName);
       } 
       catch (URISyntaxException e)  
       {
           LoginException le = new LoginException("Unable to find user DN in referral LDAP");
           le.initCause(e);
           throw le;
       }
       String name = localUserDN(absoluteName);
       String namingProviderURL = uri.getScheme() + ":
       InitialLdapContext refCtx = null;
       try 
       {
          Properties refEnv = constructLdapContextEnvironment(namingProviderURL, name, credential, null);
          refCtx = new InitialLdapContext(refEnv, null);
          refCtx.close();
       }
       catch (NamingException e)
       {
          LoginException le = new LoginException("Unable to create referral LDAP context");
          le.initCause(e);
          throw le;   
       }
   }
   private String localUserDN(String absoluteDN) {
      try 
      {
         URI userURI = new URI(absoluteDN);
         return userURI.getPath().substring(1);
      }
      catch (URISyntaxException e)
      {
         return null;
      }  
   }
   private Boolean isUserDnAbsolute(String userDN) {
      try 
      {
         URI userURI = new URI(userDN);
         return userURI.isAbsolute();
      }
      catch (URISyntaxException e)
      {
         return false;
      }  
   }
   protected void authenticate(String userDN) throws LoginException
   {
      char[] credential = getCredential();
      if (credential.length == 0)
      {
         if (allowEmptyPassword == false)
         {
            log.trace("Rejecting empty password.");
            return;
         }
      }
      if (isUserDnAbsolute(userDN))
      {
         referralAuthenticate(userDN, credential);
      }
      else 
      {
         try
         {
            LdapContext authContext = constructLdapContext(null, userDN, credential, null);
            authContext.close();
         }
         catch (NamingException ne)
         {
            if (log.isDebugEnabled())
               log.debug("Authentication failed - " + ne.getMessage());
            LoginException le = new LoginException("Authentication failed");
            le.initCause(ne);
            throw le;
         }
      }
      super.loginOk = true;
      if (getUseFirstPass() == true)
      { 
         sharedState.put("javax.security.auth.login.name", getIdentity().getName());
         sharedState.put("javax.security.auth.login.password", credential);
      }
   }
   protected void rolesSearch(LdapContext searchContext, String dn) throws LoginException
   {
      Object[] filterArgs = null;
      if (isUserDnAbsolute(dn))
      {
         filterArgs = new Object[] {getIdentity().getName(), localUserDN(dn)};
      }
      else
      {
         filterArgs = new Object[] {getIdentity().getName(), dn};
      }
      NamingEnumeration results = null;
      try
      {
         if (trace) {
            log.trace("rolesCtxDN=" + rolesCtxDN + " roleFilter=" + roleFilter + " filterArgs[0]=" + filterArgs[0]
               + " filterArgs[1]=" + filterArgs[1]);
         }
         if (roleFilter != null && roleFilter.length() > 0)
         {
            boolean referralsExist = true;
            while (referralsExist)
            {
               try
               {
                  results = searchContext.search(rolesCtxDN, roleFilter, filterArgs, roleSearchControls);
                  while (results.hasMore())
                  {
                     SearchResult sr = (SearchResult) results.next();
                     String resultDN = null;
                     if (sr.isRelative())
                     {
                        resultDN = canonicalize(sr.getName());
                     }
                     else
                     {
                        resultDN = sr.getNameInNamespace();
                     }
                     obtainRole(searchContext, resultDN, sr);
                  }
                  referralsExist = false;
               }
               catch (ReferralException e)
               {
                  searchContext = (LdapContext) e.getReferralContext();
               }
            }
         }
         else
         {
            obtainRole(searchContext, quoted(dn), null);
         }
      }
      catch (NamingException e)
      {
         LoginException le = new LoginException("Error finding roles");
         le.initCause(e);
         throw le;
      }
      finally
      {
         if (results != null)
         {
            try
            {
               results.close();
            }
            catch (NamingException e)
            {
               log.warn("Problem closing results", e);
            }
         }
      }
   }
   private String quoted(final String dn) {
       String temp = dn.trim();
       if (temp.startsWith("\"") && temp.endsWith("\"")) {
           return temp;
       }
       return "\"" + temp + "\"";
   }
   protected void obtainRole(LdapContext searchContext, String dn, SearchResult sr) throws NamingException, LoginException
   {
      if (trace) {
         log.trace("rolesSearch resultDN = " + dn);
      }
      String[] attrNames =
      {roleAttributeID};
      Attributes result = null;
      if (sr == null || sr.isRelative())
      {
         result = searchContext.getAttributes(dn, attrNames);
      }
      else
      {
         result = getAttributesFromReferralEntity(sr);
      }
      if (result != null && result.size() > 0)
      {
         Attribute roles = result.get(roleAttributeID);
         for (int n = 0; n < roles.size(); n++)
         {
            String roleName = (String) roles.get(n);
            if (roleAttributeIsDN)
            {
               String roleDN = "\"" + roleName + "\"";
               loadRoleByRoleNameAttributeID(searchContext, roleDN);
               recurseRolesSearch(searchContext, roleName);
            }
            else
            {
               addRole(roleName);
            }
         }
      }
   }
   private Attributes getAttributesFromReferralEntity(SearchResult sr) throws NamingException
   {
      Attributes result = sr.getAttributes();
      boolean chkSuccessful = false;
      if (referralUserAttributeIDToCheck != null)
      {
         Attribute usersToCheck = result.get(referralUserAttributeIDToCheck);
         for (int i = 0; usersToCheck != null && i < usersToCheck.size(); i++)
         {
            String userDNToCheck = (String) usersToCheck.get(i);
            if (userDNToCheck.equals(referralUserDNToCheck))
            {
               chkSuccessful = true;
               break;
            }
            if (userDNToCheck.equals(getIdentity().getName()))
            {
               chkSuccessful = true;
               break;
            }
         }
      }
      return (chkSuccessful ? result : null);
   }
   protected void loadRoleByRoleNameAttributeID(LdapContext searchContext, String roleDN)
   {
      String[] returnAttribute = {roleNameAttributeID};
      if (trace) {
         log.trace("Using roleDN: " + roleDN);
      }
      try
      {
         Attributes result2 = searchContext.getAttributes(roleDN, returnAttribute);
         Attribute roles2 = result2.get(roleNameAttributeID);
         if (roles2 != null)
         {
            for (int m = 0; m < roles2.size(); m++)
            {
               String roleName = (String) roles2.get(m);
               addRole(roleName);
            }
         }
      }
      catch (NamingException e)
      {
         if (trace) {
            log.trace("Failed to query roleNameAttrName", e);
         }
      }
   }
   protected void recurseRolesSearch(LdapContext searchContext, String roleDN) throws LoginException
   {
      if (recurseRoles)
      {
         if (processedRoleDNs.contains(roleDN) == false)
         {
            processedRoleDNs.add(roleDN);
            if (trace) {
               log.trace("Recursive search for '" + roleDN + "'");
            }
            rolesSearch(searchContext, roleDN);
         }
         else
         {
            if (trace) {
               log.trace("Already visited role '" + roleDN + "' ending recursion.");
            }
         }
      }
   }
   protected void traceLdapEnv(Properties env)
   {
      if (trace)
      {
         Properties tmp = new Properties();
         tmp.putAll(env);
         String credentials = tmp.getProperty(Context.SECURITY_CREDENTIALS);
         String bindCredential = tmp.getProperty(BIND_CREDENTIAL);
         if (credentials != null && credentials.length() > 0)
            tmp.setProperty(Context.SECURITY_CREDENTIALS, "***");
         if (bindCredential != null && bindCredential.length() > 0)
             tmp.setProperty(BIND_CREDENTIAL, "***");
         log.trace("Logging into LDAP server, env=" + tmp.toString());
      }
   }
   protected String canonicalize(String searchResult)
   {
      String result = searchResult;
      int len = searchResult.length();
      if (searchResult.endsWith("\""))
      {
         result = searchResult.substring(0, len - 1) + "," + rolesCtxDN + "\"";
      }
      else
      {
         result = searchResult + "," + rolesCtxDN;
      }
      return result;
   }
   private void addRole(String roleName)
   {
      if (roleName != null)
      {
         try
         {
            Principal p = super.createIdentity(roleName);
            if (trace) {
               log.trace("Assign user '" + getIdentity().getName() + "' to role " + roleName);
            }
            userRoles.addMember(p);
         }
         catch (Exception e)
         {
            if (log.isDebugEnabled())
               log.debug("Failed to create principal: " + roleName, e);
         }
      }
   }
   private class AuthorizeAction implements PrivilegedAction<Object>
   {
      public Object run()
      {
         try
         {
            return innerLogin();
         }
         catch (LoginException e)
         {
            return e;
         }
      }
   }
}
