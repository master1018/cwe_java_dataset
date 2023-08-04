@Entity
public class Customer {
private String id;
private String firstName;
private String lastName;
private Address address;
public Customer() {
}
public Customer(String id, String firstName, String lastName) {...}
@Id
public String getCustomerId() {...}
public void setCustomerId(String id) {...}
public String getFirstName() {...}
public void setFirstName(String firstName) {...}
public String getLastName() {...}
public void setLastName(String lastName) {...}
@OneToOne()
public Address getAddress() {...}
public void setAddress(Address address) {...}
}
