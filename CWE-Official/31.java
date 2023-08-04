public class BankAccount implements Cloneable {
    private String accountNumber;

    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Object clone(String accountNumber) throws CloneNotSupportedException {
        Object returnMe = new BankAccount(accountNumber);
        return returnMe;
    }
}
