
package org.opencastproject.security.impl.jpa;
import org.opencastproject.security.api.Organization;
import org.opencastproject.security.api.Role;
import org.opencastproject.security.api.User;
import org.opencastproject.util.EqualsUtil;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
@Entity
@Access(AccessType.FIELD)
@Table(name = "oc_user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username", "organization" }) })
@NamedQueries({
  @NamedQuery(name = "User.findByQuery", query = "select u from JpaUser u where UPPER(u.username) like :query and u.organization.id = :org"),
  @NamedQuery(name = "User.findByIdAndOrg", query = "select u from JpaUser u where u.id=:id and u.organization.id = :org"),
  @NamedQuery(name = "User.findByUsername", query = "select u from JpaUser u where u.username=:u and u.organization.id = :org"),
  @NamedQuery(name = "User.findAll", query = "select u from JpaUser u where u.organization.id = :org"),
  @NamedQuery(name = "User.findAllByUserNames", query = "select u from JpaUser u where u.organization.id = :org AND u.username IN :names"),
  @NamedQuery(name = "User.countAll", query = "select COUNT(u) from JpaUser u where u.organization.id = :org") })
public class JpaUser implements User {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;
  @Column(name = "username", length = 128)
  private String username;
  @Column(name = "name")
  private String name;
  @Column(name = "email")
  private String email;
  @Column(name = "manageable")
  private boolean manageable = true;
  @Transient
  private String provider;
  @Lob
  @Column(name = "password", length = 65535)
  private String password;
  @OneToOne()
  @JoinColumn(name = "organization")
  private JpaOrganization organization;
  @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
  @JoinTable(name = "oc_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") }, uniqueConstraints = { @UniqueConstraint(columnNames = {
          "user_id", "role_id" }) })
  private Set<JpaRole> roles;
  public JpaUser() {
  }
  public JpaUser(String username, String password, JpaOrganization organization, String name, String email,
          String provider, boolean manageable) {
    super();
    this.username = username;
    this.password = password;
    this.organization = organization;
    this.name = name;
    this.email = email;
    this.provider = provider;
    this.manageable = manageable;
    this.roles = new HashSet<JpaRole>();
  }
  public JpaUser(String username, String password, JpaOrganization organization, String provider, boolean manageable,
          Set<JpaRole> roles) {
    this(username, password, organization, null, null, provider, manageable);
    for (Role role : roles) {
      if (!Objects.equals(organization.getId(), role.getOrganizationId())) {
        throw new IllegalArgumentException("Role " + role + " is not from the same organization!");
      }
    }
    this.roles = roles;
  }
  public JpaUser(String username, String password, JpaOrganization organization, String name, String email,
          String provider, boolean manageable, Set<JpaRole> roles) {
    this(username, password, organization, name, email, provider, manageable);
    for (Role role : roles) {
      if (!Objects.equals(organization.getId(), role.getOrganizationId())) {
        throw new IllegalArgumentException("Role " + role + " is not from the same organization ("
                + organization.getId() + ")");
      }
    }
    this.roles = roles;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  @Override
  public String getPassword() {
    return password;
  }
  @Override
  public String getUsername() {
    return username;
  }
  @Override
  public boolean hasRole(String roleName) {
    for (Role role : roles) {
      if (role.getName().equals(roleName))
        return true;
    }
    return false;
  }
  @Override
  public Organization getOrganization() {
    return organization;
  }
  @Override
  public Set<Role> getRoles() {
    return new HashSet<Role>(roles);
  }
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof User))
      return false;
    User other = (User) obj;
    return username.equals(other.getUsername()) && organization.equals(other.getOrganization())
            && EqualsUtil.eq(provider, other.getProvider());
  }
  @Override
  public int hashCode() {
    return Objects.hash(username, organization, provider);
  }
  @Override
  public String toString() {
    return new StringBuilder(username).append(":").append(organization).append(":").append(provider).toString();
  }
  @Override
  public String getName() {
    return name;
  }
  @Override
  public String getEmail() {
    return email;
  }
  @Override
  public String getProvider() {
    return provider;
  }
  public void setProvider(String provider) {
    this.provider = provider;
  }
  @Override
  public boolean isManageable() {
    return manageable;
  }
  public void setManageable(boolean isManageable) {
    this.manageable = isManageable;
  }
}
