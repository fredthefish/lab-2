import java.lang.IllegalArgumentException;
import java.lang.IllegalAccessException;

class Driver {
    public static void main(String[] args) {
        // BankAccount test = new BankAccount("Fred", 50); // BankAccount is abstract; cannot be instantiated
        BankAccount bank;
        SavingsAccount savings;
        SavingsAccountKid kid;
        bank = new CheckingAccount("Joe", 1000);
        savings = new SavingsAccount("Bob", 5000, 0.05);
        kid = new SavingsAccountKid("Sarah", 300, 0.04, "Bob");
        System.out.println("Created three bank accounts: Joe's checking account with $1000, " +
        "Bob's checking account with $5000 and a 5% interest rate, " +
        "and Sarah (daughter of Bob)'s child saving account with $300 and a 4% interest rate.");
        
        System.out.println("Joe's balance: $" + bank.getBalance());
        System.out.println("Joe's name: " + bank.getName());
        bank.deposit(100);
        System.out.println("Joe deposited $100. His new balance: $" + bank.getBalance());
        try { bank.deposit(-100); }
        catch(IllegalArgumentException e) { System.out.println("Joe cannot deposit negative money."); }
        try { bank.deposit(0); }
        catch(IllegalArgumentException e) { System.out.println("Joe cannot deposit no money."); }
        try { bank.withdraw(-100); }
        catch(IllegalArgumentException e) { System.out.println("Joe cannot withdraw negative money."); }
        try { bank.withdraw(bank.getBalance() + 1); }
        catch(IllegalArgumentException e) { System.out.println("Joe cannot withdraw more than he has."); }
        bank.withdraw(100);
        System.out.println("Joe withdrew $100. His new balance: " + bank.getBalance());
        savings.transfer(100, bank);
        System.out.println("Bob sent Joe $100. Joe's new balance: $" + bank.getBalance()
         + ". Bob's new balance :$" + savings.getBalance());
        try { bank.transfer(bank.getBalance() + 1, kid); }
        catch(IllegalArgumentException e) { System.out.println("Joe cannot transfer more than he has."); }
        for(int i = 0; i < 7; i++) {
          try { savings.withdraw(i); System.out.println("Bob withdrew $"+i+". His new balance: $" + savings.getBalance()
          + ". Withdraws left: " + savings.getWithdrawCount()); }
          catch(IllegalStateException e) { System.out.println("Bob has used up all of his withdraws."); }
          catch(IllegalArgumentException e) { System.out.println("Bob cannot withdraw more than he has or negative money."); }
        }
        savings.addInterest();
        System.out.println("Bob collected interest. His new balance: $" + savings.getBalance());
        try { kid.withdraw(100, "Sarah"); }
        catch(IllegalAccessException e) { System.out.println("Sarah tried to withdraw, but is a child and cannot."); }
        try { kid.transfer(100, savings, "Bob"); System.out.println("Bob took $100 from Sarah's account. " +
        "His balance: $" + savings.getBalance() + ". Her balance: $" + kid.getBalance() + "."); }
        catch(IllegalAccessException e) { System.out.println("Bob does not have access to Sarah's account."); }
        
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
    if (withdrawCount == 6) throw new IllegalStateException();
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
  public void transfer(double amount, BankAccount destination, String parentName) throws IllegalAccessException {
    if (!parentName.equals(this.parentName)) throw new IllegalAccessException();
    super.transfer(amount, destination);
  }
  public void withdraw(double amount, String parentName) throws IllegalAccessException {
    if (!parentName.equals(this.parentName)) throw new IllegalAccessException();
    super.withdraw(amount);
  }
}
