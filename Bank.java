import java.lang.IllegalArgumentException;
import java.lang.IllegalAccessException;

public class Driver {
    public static void main(String[] args) {
        // BankAccount test = new BankAccount("Fred", 50); // BankAccount is abstract; cannot be instantiated
        BankAccount bank;
        BankAccount savings;
        BankAccount kid;
        bank = new CheckingAccount("Joe", 1000);
        savings = new SavingsAccount("Bob", 5000, 0.05);
        kid = new SavingsAccountKid("Sarah", 300, 0.04, "Bob");
        System.out.println("Created three bank accounts: Joe's checking account with $1000, " +
        "Bob's checking account with $5000 and a 5% interest rate, " +
        "and Sarah (daughter of Bob)'s child saving account with $300 and a 4% interest rate.");
        
        System.out.println("Joe's balance: " + bank.getBalance());
        System.out.println("Joe's name: " + bank.getName());
        bank.deposit(100);
        System.out.println("Joe deposited $100. His new balance: " + bank.getBalance());
        try { bank.deposit(-100); }
        catch(IllegalArgumentException e) { System.out.println("Joe cannot deposit negative money."); }
        try { bank.deposit(0); }
        catch(IllegalArgumentException e) { System.out.println("Joe cannot deposit no money."); }

    }
}

interface BankAccountInterface {
  double getBalance() throws Exception;
  String getName() throws Exception;
  void deposit(double amount) throws Exception;
  void withdraw(double amount) throws Exception;
  void transfer(double amount, BankAccount destination) throws Exception;
}

abstract class BankAccount implements BankAccountInterface {
  private String name;
  private double balance;
  
  public BankAccount(String name, double initialDeposit) {
    if (name.length() <= 2) throw new IllegalArgumentException();
    this.name = name;
    balance = initialDeposit;
    if (balance <= 0) throw new IllegalArgumentException();
  }
  public void deposit(double amount) {
    if (amount <= 0) throw new IllegalArgumentException();
    balance += amount;
  }
  public void withdraw(double amount) {
    if (amount <= 0) throw new IllegalArgumentException();
    if (amount > balance) throw new IllegalArgumentException();
    balance -= amount;
  }
  public void transfer(double amount, BankAccount destination) {
    withdraw(amount);
    destination.balance += amount;
  }
  public double getBalance() { return balance; }
  public String getName() { return name; }
}

class CheckingAccount extends BankAccount {
  private String name;
  private double balance;
  
  public CheckingAccount(String name, double initialDeposit) {
    super(name, initialDeposit);
  }
}

class SavingsAccount extends BankAccount {
  private String name;
  private double balance;
  private double interestRate;
  private int withdrawCount;
  
  public SavingsAccount(String name, double initialDeposit, double interestRate) {
    super(name, initialDeposit);
    this.interestRate = interestRate;
    if (interestRate < 0) throw new IllegalArgumentException();
    withdrawCount = 0;
  }
  public void addInterest() {
    balance *= 1 + interestRate;
  }
  public int getWithdrawCount() {
    return withdrawCount;
  }
  public void withdraw(double amount) {
    if (withdrawCount == 6) { throw new IllegalAccessException(); }
    super.withdraw(amount);
    withdrawCount++;
  }
}

class SavingsAccountKid extends SavingsAccount {
  private String name;
  private double balance;
  private double interestRate;
  private int withdrawCount;
  private String parentName;
  
  public SavingsAccountKid(String name, double initialDeposit, double interestRate, String parentName) {
    super(name, initialDeposit, interestRate);
    this.parentName = parentName;
  }
  
  public void withdraw(double amount, String parentName) {
    if (!parentName.equals(this.parentName)) throw new IllegalAccessException();
    super.withdraw(amount);
  }
}
