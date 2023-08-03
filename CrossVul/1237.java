
package org.opencastproject.userdirectory;
import org.opencastproject.security.api.Group;
import org.opencastproject.security.api.Role;
import org.opencastproject.security.api.RoleProvider;
import org.opencastproject.security.api.SecurityService;
import org.opencastproject.security.api.UnauthorizedException;
import org.opencastproject.security.api.User;
import org.opencastproject.security.api.UserProvider;
import org.opencastproject.security.impl.jpa.JpaOrganization;
import org.opencastproject.security.impl.jpa.JpaRole;
import org.opencastproject.security.impl.jpa.JpaUser;
import org.opencastproject.userdirectory.utils.UserDirectoryUtils;
import org.opencastproject.util.NotFoundException;
import org.opencastproject.util.PasswordEncoder;
import org.opencastproject.util.data.Monadics;
import org.opencastproject.util.data.Option;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
public class JpaUserAndRoleProvider implements UserProvider, RoleProvider {
  private static final Logger logger = LoggerFactory.getLogger(JpaUserAndRoleProvider.class);
  public static final String PERSISTENCE_UNIT = "org.opencastproject.common";
  public static final String PROVIDER_NAME = "opencast";
  public static final String USERNAME = "username";
  public static final String ROLES = "roles";
  public static final String ENCODING = "UTF-8";
  private static final String DELIMITER = ";==;";
  protected SecurityService securityService = null;
  protected JpaGroupRoleProvider groupRoleProvider;
  private LoadingCache<String, Object> cache = null;
  protected Object nullToken = new Object();
  void setEntityManagerFactory(EntityManagerFactory emf) {
    this.emf = emf;
  }
  public void setSecurityService(SecurityService securityService) {
    this.securityService = securityService;
  }
  void setGroupRoleProvider(JpaGroupRoleProvider groupRoleProvider) {
    this.groupRoleProvider = groupRoleProvider;
  }
  protected EntityManagerFactory emf = null;
  public void activate(ComponentContext cc) {
    logger.debug("activate");
    cache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build(new CacheLoader<String, Object>() {
      @Override
      public Object load(String id) {
        String[] key = id.split(DELIMITER);
        logger.trace("Loading user '{}':'{}' from database", key[0], key[1]);
        User user = loadUser(key[0], key[1]);
        return user == null ? nullToken : user;
      }
    });
  }
  @Override
  public List<Role> getRolesForUser(String userName) {
    ArrayList<Role> roles = new ArrayList<Role>();
    User user = loadUser(userName);
    if (user == null)
      return roles;
    roles.addAll(user.getRoles());
    return roles;
  }
  @Override
  public Iterator<User> findUsers(String query, int offset, int limit) {
    if (query == null)
      throw new IllegalArgumentException("Query must be set");
    String orgId = securityService.getOrganization().getId();
    List<JpaUser> users = UserDirectoryPersistenceUtil.findUsersByQuery(orgId, query, limit, offset, emf);
    return Monadics.mlist(users).map(addProviderName).iterator();
  }
  @Override
  public Iterator<User> findUsers(Collection<String> userNames) {
    String orgId = securityService.getOrganization().getId();
    List<JpaUser> users = UserDirectoryPersistenceUtil.findUsersByUserName(userNames, orgId, emf);
    return Monadics.mlist(users).map(addProviderName).iterator();
  }
  @Override
  public Iterator<Role> findRoles(String query, Role.Target target, int offset, int limit) {
    if (query == null)
      throw new IllegalArgumentException("Query must be set");
    return new ArrayList<Role>().iterator();
  }
  @Override
  public User loadUser(String userName) {
    String orgId = securityService.getOrganization().getId();
    Object user = cache.getUnchecked(userName.concat(DELIMITER).concat(orgId));
    if (user == nullToken) {
      return null;
    } else {
      return (User) user;
    }
  }
  @Override
  public Iterator<User> getUsers() {
    String orgId = securityService.getOrganization().getId();
    List<JpaUser> users = UserDirectoryPersistenceUtil.findUsers(orgId, 0, 0, emf);
    return Monadics.mlist(users).map(addProviderName).iterator();
  }
  @Override
  public String getOrganization() {
    return ALL_ORGANIZATIONS;
  }
  @Override
  public String toString() {
    return getClass().getName();
  }
  public User loadUser(String userName, String organization) {
    JpaUser user = UserDirectoryPersistenceUtil.findUser(userName, organization, emf);
    return Option.option(user).map(addProviderName).getOrElseNull();
  }
  public User loadUser(long userId, String organization) {
    JpaUser user = UserDirectoryPersistenceUtil.findUser(userId, organization, emf);
    return Option.option(user).map(addProviderName).getOrElseNull();
  }
  public void addUser(JpaUser user) throws UnauthorizedException {
    if (!UserDirectoryUtils.isCurrentUserAuthorizedHandleRoles(securityService, user.getRoles()))
      throw new UnauthorizedException("The user is not allowed to set the admin role on other users");
    String encodedPassword = PasswordEncoder.encode(user.getPassword(), user.getUsername());
    Set<JpaRole> roles = UserDirectoryPersistenceUtil.saveRoles(filterRoles(user.getRoles()), emf);
    JpaOrganization organization = UserDirectoryPersistenceUtil.saveOrganization(
            (JpaOrganization) user.getOrganization(), emf);
    JpaUser newUser = new JpaUser(user.getUsername(), encodedPassword, organization, user.getName(), user.getEmail(),
            user.getProvider(), user.isManageable(), roles);
    EntityManager em = null;
    EntityTransaction tx = null;
    try {
      em = emf.createEntityManager();
      tx = em.getTransaction();
      tx.begin();
      em.persist(newUser);
      tx.commit();
      cache.put(user.getUsername() + DELIMITER + user.getOrganization().getId(), newUser);
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      if (em != null)
        em.close();
    }
    updateGroupMembership(user);
  }
  public User updateUser(JpaUser user) throws NotFoundException, UnauthorizedException {
    if (!UserDirectoryUtils.isCurrentUserAuthorizedHandleRoles(securityService, user.getRoles()))
      throw new UnauthorizedException("The user is not allowed to set the admin role on other users");
    JpaUser updateUser = UserDirectoryPersistenceUtil.findUser(user.getUsername(), user.getOrganization().getId(), emf);
    if (updateUser == null) {
      throw new NotFoundException("User " + user.getUsername() + " not found.");
    }
    logger.debug("updateUser({})", user.getUsername());
    if (!UserDirectoryUtils.isCurrentUserAuthorizedHandleRoles(securityService, updateUser.getRoles()))
      throw new UnauthorizedException("The user is not allowed to update an admin user");
    String encodedPassword;
    if (StringUtils.isEmpty(user.getPassword())) {
      encodedPassword = updateUser.getPassword();
    } else  {
      encodedPassword = PasswordEncoder.encode(user.getPassword(), user.getUsername());
    }
    Set<JpaRole> roles = UserDirectoryPersistenceUtil.saveRoles(filterRoles(user.getRoles()), emf);
    JpaOrganization organization = UserDirectoryPersistenceUtil.saveOrganization(
            (JpaOrganization) user.getOrganization(), emf);
    JpaUser updatedUser = UserDirectoryPersistenceUtil.saveUser(
            new JpaUser(user.getUsername(), encodedPassword, organization, user.getName(), user.getEmail(), user
                    .getProvider(), true, roles), emf);
    cache.put(user.getUsername() + DELIMITER + organization.getId(), updatedUser);
    updateGroupMembership(user);
    return updatedUser;
  }
  private Set<JpaRole> filterRoles(Set<Role> userRoles) {
    Set<JpaRole> roles = new HashSet<JpaRole>();
    for (Role role : userRoles) {
      if (Role.Type.INTERNAL.equals(role.getType()) && !role.getName().startsWith(Group.ROLE_PREFIX)) {
        JpaRole jpaRole = (JpaRole) role;
        roles.add(jpaRole);
      }
    }
    return roles;
  }
  private void updateGroupMembership(JpaUser user) {
    logger.debug("updateGroupMembership({}, roles={})", user.getUsername(), user.getRoles().size());
    List<String> internalGroupRoles = new ArrayList<String>();
    for (Role role : user.getRoles()) {
      if (Role.Type.GROUP.equals(role.getType())
          || (Role.Type.INTERNAL.equals(role.getType()) && role.getName().startsWith(Group.ROLE_PREFIX))) {
        internalGroupRoles.add(role.getName());
      }
    }
    groupRoleProvider.updateGroupMembershipFromRoles(user.getUsername(), user.getOrganization().getId(), internalGroupRoles);
  }
  public void deleteUser(String username, String orgId) throws NotFoundException, UnauthorizedException, Exception {
    User user = loadUser(username, orgId);
    if (user != null && !UserDirectoryUtils.isCurrentUserAuthorizedHandleRoles(securityService, user.getRoles()))
      throw new UnauthorizedException("The user is not allowed to delete an admin user");
    groupRoleProvider.updateGroupMembershipFromRoles(username, orgId, new ArrayList<String>());
    UserDirectoryPersistenceUtil.deleteUser(username, orgId, emf);
    cache.invalidate(username + DELIMITER + orgId);
  }
  public void addRole(JpaRole jpaRole) {
    HashSet<JpaRole> roles = new HashSet<JpaRole>();
    roles.add(jpaRole);
    UserDirectoryPersistenceUtil.saveRoles(roles, emf);
  }
  @Override
  public String getName() {
    return PROVIDER_NAME;
  }
  private static org.opencastproject.util.data.Function<JpaUser, User> addProviderName = new org.opencastproject.util.data.Function<JpaUser, User>() {
    @Override
    public User apply(JpaUser a) {
      a.setProvider(PROVIDER_NAME);
      return a;
    }
  };
  @Override
  public long countUsers() {
    String orgId = securityService.getOrganization().getId();
    return UserDirectoryPersistenceUtil.countUsers(orgId, emf);
  }
  @Override
  public void invalidate(String userName) {
    String orgId = securityService.getOrganization().getId();
    cache.invalidate(userName + DELIMITER + orgId);
  }
}
