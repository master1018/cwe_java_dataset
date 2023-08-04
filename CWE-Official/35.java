public class BankAccount {
private String accountOwnerName;
private String accountOwnerSSN;
private int accountNumber;
private double balance;
public BankAccount(String accountOwnerName, String accountOwnerSSN,
int accountNumber, double initialBalance, int initialRate)
{
this.accountOwnerName = accountOwnerName;
this.accountOwnerSSN = accountOwnerSSN;
this.accountNumber = accountNumber;
this.balance = initialBalance;
this.start(initialRate);
}
public void start(double rate)
{
ActionListener adder = new InterestAdder(rate);
Timer t = new Timer(1000 * 3600 * 24 * 30, adder);
t.start();
}
private class InterestAdder implements ActionListener
{
private double rate;
public InterestAdder(double aRate)
{
this.rate = aRate;
}
public void actionPerformed(ActionEvent event)
{
double interest = BankAccount.this.balance * rate / 100;
BankAccount.this.balance += interest;
}
}
}
