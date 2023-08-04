@Entity
public class Customer implements Serializable {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @OneToOne
    private Address address;

    public Customer() {
        // Replace with implementation
    }

    public Customer(String id, String firstName, String lastName) {
        // Replace with implementation
    }

    public String getCustomerId() {
        // Replace with implementation
        return id;
    }

    public synchronized void setCustomerId(String id) {
        // Replace with implementation
        this.id = id;
    }

    public String getFirstName() {
        // Replace with implementation
        return firstName;
    }

    public synchronized void setFirstName(String firstName) {
        // Replace with implementation
        this.firstName = firstName;
    }

    public String getLastName() {
        // Replace with implementation
        return lastName;
    }

    public synchronized void setLastName(String lastName) {
        // Replace with implementation
        this.lastName = lastName;
    }

    public Address getAddress() {
        // Replace with implementation
        return address;
    }

    public synchronized void setAddress(Address address) {
        // Replace with implementation
        this.address = address;
    }
}
