public class BankAccount implements Cloneable {
    private String accountNumber;

    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

